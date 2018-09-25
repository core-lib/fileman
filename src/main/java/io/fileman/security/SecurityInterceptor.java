package io.fileman.security;

import io.detector.Filter;
import io.detector.FilterChain;
import io.detector.SimpleDetector;
import io.fileman.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * 安全认证拦截器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/25
 */
public class SecurityInterceptor implements Interceptor, Initialable, Filter {
    private final SAXReader reader = new SAXReader();
    private final Map<String, Resource> resources = new LinkedHashMap<>();
    private final Map<String, Role> roles = new LinkedHashMap<>();
    private final Map<String, User> users = new LinkedHashMap<>();
    private Authenticator authenticator;

    @Override
    public void initialize(Configuration configuration) throws Exception {
        String location = configuration.valueOf("security-config-location");
        if (Toolkit.isBlank(location)) location = "fileman-security.xml";
        String[] locations = location.split(SPLIT_DELIMIT_REGEX);
        ClassLoader classLoader = this.getClass().getClassLoader();
        for (String config : locations) {
            URL url = classLoader.getResource(config);
            if (url == null) throw new Exception("could not found config " + config);
            read(url);
        }
        for (Role role : roles.values()) {
            Element element = role.getElement();
            String value = element.attributeValue("resources");
            if (value == null) continue;
            String[] names = value.split(SPLIT_DELIMIT_REGEX);
            for (String name : names) {
                Resource resource = resources.get(name);
                if (resource == null) throw new Exception("unknown resource named " + name);
                role.getResources().add(resource);
            }
        }
        for (User user : users.values()) {
            Element element = user.getElement();
            String value = element.attributeValue("roles");
            if (value == null) continue;
            String[] names = value.split(SPLIT_DELIMIT_REGEX);
            for (String name : names) {
                Role role = roles.get(name);
                if (role == null) throw new Exception("unknown role named " + name);
                user.getRoles().add(role);
            }
        }
        {
            String algorithm = configuration.valueOf("security-auth-type", "Basic");
            Collection<io.detector.Resource> resources = SimpleDetector.Builder
                    .scan("fileman")
                    .includeJar()
                    .recursively()
                    .build()
                    .detect(this);
            Properties properties = new Properties();
            for (io.detector.Resource resource : resources) {
                InputStream in = resource.getInputStream();
                properties.load(in);
            }
            String className = properties.getProperty(algorithm);
            authenticator = Class.forName(className).asSubclass(Authenticator.class).newInstance();
        }
    }

    private void read(URL url) throws DocumentException {
        Document document = reader.read(url);
        Element root = document.getRootElement();
        List<?> elements = root.elements();
        for (Object obj : elements) {
            Element element = (Element) obj;
            String type = element.getName();
            switch (type) {
                case "resource": {
                    String name = element.attributeValue("name");
                    resources.put(name, new Resource(element));
                }
                break;
                case "role": {
                    String name = element.attributeValue("name");
                    roles.put(name, new Role(element));
                }
                break;
                case "user": {
                    String name = element.attributeValue("name");
                    users.put(name, new User(element));
                }
                break;
                default: {
                    throw new DocumentException("unexpected element named " + type);
                }
            }
        }
    }

    @Override
    public void intercept(File file, InterceptContext context) throws Exception {
        try {
            authenticator.authenticate(file, new AuthenticateContext(context, users));
            context.doNext(file);
        } catch (UnauthorizedSecurityException e) {
            HttpServletResponse response = context.getResponse();
            response.setHeader("WWW-Authenticate", authenticator.algorithm());
            response.sendError(HttpURLConnection.HTTP_UNAUTHORIZED, "Unauthorized");
        } catch (ForbiddenSecurityException e) {
            HttpServletResponse response = context.getResponse();
            response.sendError(HttpURLConnection.HTTP_FORBIDDEN, "Forbidden");
        }
    }

    @Override
    public boolean accept(io.detector.Resource resource, FilterChain chain) {
        return "authenticator.properties".equals(resource.getName()) && chain.doNext(resource);
    }
}
