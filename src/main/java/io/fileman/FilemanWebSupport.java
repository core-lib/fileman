package io.fileman;

import io.detector.Filter;
import io.detector.FilterChain;
import io.detector.Resource;
import io.detector.SimpleDetector;
import io.fileman.formatter.HtmlFormatter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.*;

/**
 * 文件管理器WEB集成
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public class FilemanWebSupport {
    protected Configuration configuration;
    protected String root;
    protected List<Converter> converters = new ArrayList<>();
    protected Synthesizer<Converter> synthesizer;
    protected Formatter formatter;

    protected void init(Configuration configuration) throws ServletException {
        try {
            this.configuration = configuration;
            root = Filemans.ifBlank(configuration.valueOf("root"), System.getProperty("user.dir"));
            synthesizer = Filemans.newInstance(Filemans.ifBlank(configuration.valueOf("synthesizer"), RenderSynthesizer.class.getName()));
            formatter = Filemans.newInstance(Filemans.ifBlank(configuration.valueOf("formatter"), HtmlFormatter.class.getName()));
            String fields = configuration.valueOf("fields");
            String[] columns = fields.split("[,\\s\r\n]+");
            Collection<Resource> resources = SimpleDetector.Builder
                    .scan("fileman")
                    .includeJar()
                    .recursively()
                    .build()
                    .detect(new ConfigFilter());
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

    protected void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestPath = request.getRequestURI();
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String filemanPath = requestPath.substring(contextPath.length() + servletPath.length());
        while (filemanPath.endsWith("/")) filemanPath = filemanPath.substring(0, filemanPath.length() - 1);
        filemanPath = URLDecoder.decode(filemanPath, "UTF-8");
        File file = new File(root, filemanPath);
        // 文件不存在
        if (!file.exists()) {
            response.sendError(HttpURLConnection.HTTP_NOT_FOUND, "Not Found");
        }
        // 是目录
        else if (file.isDirectory()) {
            File root = new File(this.root);
            Fileman fileman = new Fileman();
            fileman.setUri(("/" + contextPath + "/" + servletPath + "/" + filemanPath).replaceAll("/+", "/"));
            fileman.setPath(filemanPath);
            fileman.setFolder(true);
            fileman.setChildren(new ArrayList<Fileman>());
            File[] files = file.listFiles();
            for (int i = 0; files != null && i < files.length; i++) {
                File sub = files[i];
                Fileman child = new Fileman();
                child.setUri(("/" + contextPath + "/" + servletPath + "/" + filemanPath + "/" + sub.getName()).replaceAll("/+", "/"));
                child.setPath(filemanPath + "/" + sub.getName());
                child.setFolder(sub.isDirectory());
                SynthesizeContext<Converter> context = new SynthesizeContext<>(root, configuration, request, response, converters);
                Map<String, Object> properties = synthesizer.synthesize(sub, context);
                child.setProperties(properties);
                fileman.getChildren().add(child);
            }
            SynthesizeContext<Converter> context = new SynthesizeContext<>(root, configuration, request, response, converters);
            Map<String, Object> properties = synthesizer.synthesize(file, context);
            fileman.setProperties(properties);

            formatter.format(fileman, new FormatContext(root, configuration, request, response));
        }
        // 是文件
        else if (file.isFile()) {

        }
        // 不认识
        else {
            response.sendError(HttpURLConnection.HTTP_NOT_FOUND, "Not Found");
        }
    }

    protected void destroy() {
        this.configuration = null;
        this.root = null;
        this.converters.clear();
        this.converters = null;
        this.synthesizer = null;
        this.formatter = null;
    }

    static class ConfigFilter implements Filter {
        @Override
        public boolean accept(Resource resource, FilterChain chain) {
            return resource.getName().endsWith(".properties") && chain.doNext(resource);
        }
    }
}
