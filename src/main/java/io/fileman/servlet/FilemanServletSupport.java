package io.fileman.servlet;

import io.detector.Filter;
import io.detector.FilterChain;
import io.detector.Resource;
import io.detector.SimpleDetector;
import io.fileman.*;
import io.fileman.Formatter;
import io.fileman.formatter.HtmlFormatter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.*;

/**
 * 文件管理器Servlet集成
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public class FilemanServletSupport implements Servlet, Filter {
    private ServletConfig servletConfig;
    private String root;
    private List<Converter> converters = new ArrayList<Converter>();
    private Synthesizer synthesizer = new RenderingSynthesizer();
    private Formatter formatter = new HtmlFormatter();

    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            servletConfig = config;
            root = Filemans.ifBlank(config.getInitParameter("root"), System.getProperty("user.dir"));

            String fields = config.getInitParameter("fields");
            String[] columns = fields.split("[,\\s\r\n]+");
            Collection<Resource> resources = SimpleDetector.Builder
                    .scan("fileman")
                    .includeJar()
                    .recursively()
                    .build()
                    .detect(this);
            Properties properties = new Properties();
            for (Resource resource : resources) {
                InputStream in = resource.getInputStream();
                properties.load(in);
            }
            for (String column : columns) {
                String className = properties.getProperty(column);
                if (className == null) className = column;
                try {
                    Class<? extends Converter> clazz = Class.forName(className).asSubclass(Converter.class);
                    Converter converter = clazz.newInstance();
                    converters.add(converter);
                } catch (Exception e) {
                    throw new ServletException("unknown field " + column);
                }
            }
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    @Override
    public ServletConfig getServletConfig() {
        return servletConfig;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String requestPath = request.getRequestURI();
        String servletPath = request.getServletPath();
        String filemanPath = requestPath.substring(servletPath.length());
        while (filemanPath.endsWith("/")) filemanPath = filemanPath.substring(0, filemanPath.length() - 1);
        File file = new File(root, filemanPath);
        // 文件不存在
        if (!file.exists()) {
            response.sendError(HttpURLConnection.HTTP_NOT_FOUND, "Not Found");
        }
        // 是目录
        else if (file.isDirectory()) {
            Configuration configuration = new ServletConfiguration(servletConfig);
            File root = new File(this.root);
            Fileman fileman = new Fileman();
            fileman.setPath(servletPath + filemanPath);
            fileman.setFolder(true);
            fileman.setChildren(new ArrayList<Fileman>());
            File[] files = file.listFiles();
            for (int i = 0; files != null && i < files.length; i++) {
                File sub = files[i];
                Fileman child = new Fileman();
                child.setPath(servletPath + filemanPath + "/" + sub.getName());
                child.setFolder(sub.isDirectory());
                Synthesization<Converter> synthesization = new Synthesization<Converter>(configuration, root, sub, converters);
                Map<String, Object> properties = synthesizer.synthesize(synthesization);
                child.setProperties(properties);
                fileman.getChildren().add(child);
            }
            Synthesization<Converter> synthesization = new Synthesization<Converter>(configuration, root, file, converters);
            Map<String, Object> properties = synthesizer.synthesize(synthesization);
            fileman.setProperties(properties);

            String contentType = formatter.getContentType();
            response.setContentType(contentType);
            OutputStream out = response.getOutputStream();
            formatter.format(fileman, out);
            out.flush();
        }
        // 是文件
        else if (file.isFile()) {

        }
        // 不认识
        else {
            response.sendError(HttpURLConnection.HTTP_NOT_FOUND, "Not Found");
        }
    }

    @Override
    public String getServletInfo() {
        return servletConfig.getServletName();
    }

    @Override
    public void destroy() {
        servletConfig = null;
        root = null;
    }

    @Override
    public boolean accept(Resource resource, FilterChain filterChain) {
        return resource.getName().endsWith(".properties") && filterChain.doNext(resource);
    }
}
