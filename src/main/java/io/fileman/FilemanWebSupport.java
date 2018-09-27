package io.fileman;

import io.detector.Filter;
import io.detector.FilterChain;
import io.detector.Resource;
import io.detector.SimpleDetector;
import io.fileman.formatter.HtmlFormatter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * 文件管理器WEB集成
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public class FilemanWebSupport implements Interceptor {
    protected Configuration configuration;
    protected String root;
    protected Synthesizer<Converter> synthesizer;
    protected Formatter formatter;
    protected int buffer;
    protected List<Converter> converters = new ArrayList<>();
    protected List<Extractor> extractors = new ArrayList<>();
    protected List<Interceptor> interceptors = new ArrayList<>();

    protected void init(Configuration configuration) throws ServletException {
        try {
            this.configuration = configuration;
            root = Toolkit.ifBlank(configuration.valueOf("root"), System.getProperty("user.dir"));
            synthesizer = Toolkit.newInstance(Toolkit.ifBlank(configuration.valueOf("synthesizer"), RenderSynthesizer.class.getName()));
            formatter = Toolkit.newInstance(Toolkit.ifBlank(configuration.valueOf("formatter"), HtmlFormatter.class.getName()));
            buffer = Integer.valueOf(Toolkit.ifBlank(configuration.valueOf("buffer"), "" + 1024 * 8));
            initConverters(configuration);
            initExtractors(configuration);
            initInterceptors(configuration);
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }

    private void initConverters(Configuration configuration) throws IOException, ServletException {
        converters.clear();
        Collection<Resource> resources = SimpleDetector.Builder
                .scan("fileman")
                .includeJar()
                .recursively()
                .build()
                .detect(new ConverterConfigFilter());
        Properties properties = new Properties();
        for (Resource resource : resources) {
            InputStream in = resource.getInputStream();
            properties.load(in);
        }
        String fields = configuration.valueOf("fields");
        Iterable<String> columns = Toolkit.isBlank(fields) ? properties.stringPropertyNames() : Arrays.asList(fields.split(SPLIT_DELIMIT_REGEX));
        for (String column : columns) {
            String className = properties.getProperty(column);
            if (className == null) className = column;
            try {
                Class<? extends Converter> clazz = Class.forName(className).asSubclass(Converter.class);
                Converter converter = clazz.newInstance();
                if (converter instanceof Initialable) ((Initialable) converter).initialize(configuration);
                converters.add(converter);
            } catch (Exception e) {
                throw new ServletException("unknown field " + column);
            }
        }
    }

    private void initExtractors(Configuration configuration) throws IOException, ServletException {
        extractors.clear();
        Collection<Resource> resources = SimpleDetector.Builder
                .scan("fileman")
                .includeJar()
                .recursively()
                .build()
                .detect(new ExtractorConfigFilter());
        Properties properties = new Properties();
        for (Resource resource : resources) {
            InputStream in = resource.getInputStream();
            properties.load(in);
        }
        String ranges = configuration.valueOf("ranges");
        Iterable<String> units = Toolkit.isBlank(ranges) ? properties.stringPropertyNames() : Arrays.asList(ranges.split(SPLIT_DELIMIT_REGEX));
        for (String unit : units) {
            try {
                String className = properties.getProperty(unit);
                if (className == null) className = unit;
                Class<? extends Extractor> clazz = Class.forName(className).asSubclass(Extractor.class);
                Extractor extractor = clazz.newInstance();
                if (extractor instanceof Initialable) ((Initialable) extractor).initialize(configuration);
                extractors.add(extractor);
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }
    }

    private void initInterceptors(Configuration configuration) throws IOException, ServletException {
        interceptors.clear();
        Collection<Resource> resources = SimpleDetector.Builder
                .scan("fileman")
                .includeJar()
                .recursively()
                .build()
                .detect(new InterceptorConfigFilter());
        Properties properties = new Properties();
        for (Resource resource : resources) {
            InputStream in = resource.getInputStream();
            properties.load(in);
        }
        String value = configuration.valueOf("interceptors");
        Iterable<String> names = Toolkit.isBlank(value) ? Collections.<String>emptyList() : Arrays.asList(value.split(SPLIT_DELIMIT_REGEX));
        for (String name : names) {
            try {
                String className = properties.getProperty(name);
                if (className == null) className = name;
                Class<? extends Interceptor> clazz = Class.forName(className).asSubclass(Interceptor.class);
                Interceptor interceptor = clazz.newInstance();
                if (interceptor instanceof Initialable) ((Initialable) interceptor).initialize(configuration);
                this.interceptors.add(interceptor);
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }
        this.interceptors.add(this);
    }

    protected void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String requestPath = request.getRequestURI();
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String filemanPath = requestPath.substring(contextPath.length() + servletPath.length());
        while (filemanPath.endsWith("/")) filemanPath = filemanPath.substring(0, filemanPath.length() - 1);
        filemanPath = URLDecoder.decode(filemanPath, "UTF-8");
        File file = new File(root, filemanPath);
        InterceptContext context = new InterceptContext(new File(root), configuration, request, response, interceptors.iterator());
        context.doNext(file);
    }

    @Override
    public void intercept(File file, InterceptContext context) throws Exception {
        HttpServletRequest request = context.getRequest();
        HttpServletResponse response = context.getResponse();
        String method = request.getMethod().toUpperCase();
        switch (method) {
            case "GET":
                get(request, response);
                break;
            case "POST":
                post(request, response);
                break;
            case "PUT":
                put(request, response);
                break;
            case "DELETE":
                delete(request, response);
                break;
            default:
                response.sendError(HttpURLConnection.HTTP_BAD_METHOD, "Method Not Allowed");
                break;
        }
    }

    protected void get(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
            String range = request.getHeader("Range");
            List<String> units = new ArrayList<>();
            for (Extractor extractor : extractors) units.add(extractor.unit());
            response.setHeader("Accept-Ranges", Toolkit.join(units, ", "));
            // 全部读取
            if (Toolkit.isBlank(range)) {
                Path path = Paths.get(file.toURI());
                String contentType = Files.probeContentType(path);
                if (contentType == null) contentType = "application/octet-stream";
                response.setContentType(contentType);

                String name = file.getName();
                String contentDisposition = "attachment; filename=\"" + URLEncoder.encode(name, "UTF-8") + "\"";
                response.setHeader("Content-Disposition", contentDisposition);

                OutputStream out = response.getOutputStream();
                InputStream in = null;
                try {
                    in = new FileInputStream(file);
                    byte[] buf = new byte[buffer];
                    int len;
                    while ((len = in.read(buf)) != -1) out.write(buf, 0, len);
                } finally {
                    Toolkit.close(in);
                }
            }
            // 部分读取
            else {
                Range r = Range.valueOf(range);
                Extractor extractor = null;
                for (Extractor e : extractors) if (r.getUnit().equals(e.unit())) extractor = e;
                if (extractor == null) response.sendError(HttpURLConnection.HTTP_NOT_IMPLEMENTED, "Not Implemented");
                else extractor.extract(file, r, new ExtractContext(new File(root), configuration, request, response));
            }
        }
        // 不认识
        else {
            response.sendError(HttpURLConnection.HTTP_NOT_FOUND, "Not Found");
        }
    }

    protected void post(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String requestPath = request.getRequestURI();
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String filemanPath = requestPath.substring(contextPath.length() + servletPath.length());
        while (filemanPath.endsWith("/")) filemanPath = filemanPath.substring(0, filemanPath.length() - 1);
        filemanPath = URLDecoder.decode(filemanPath, "UTF-8");
        File file = new File(root, filemanPath);
        Collection<Part> parts = request.getParts();
        // 如果没有文件则返回
        if (parts.isEmpty()) {
            return;
        }
        // 如果文件夹不存在则创建
        if (!file.exists() && !file.mkdirs()) {
            response.sendError(HttpURLConnection.HTTP_BAD_REQUEST, "Bad Request");
            return;
        }
        // 如果当前路径不是一个文件夹则冲突
        if (!file.isDirectory()) {
            response.sendError(HttpURLConnection.HTTP_CONFLICT, "Conflict");
            return;
        }
        // 将文件写入该文件夹
        for (Part part : parts) {
            String disposition = part.getHeader("Content-Disposition");
            String[] segments = disposition.split("\\s*;\\s*");
            String filename = UUID.randomUUID().toString();
            for (String segment : segments) {
                String[] keyValue = segment.split("\\s*=\\s*");
                if (!"filename".equals(keyValue[0])) continue;
                filename = Toolkit.unquote(keyValue[1]);
            }
            part.write(new File(file, filename).getPath());
        }
    }

    protected void put(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String requestPath = request.getRequestURI();
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String filemanPath = requestPath.substring(contextPath.length() + servletPath.length());
        while (filemanPath.endsWith("/")) filemanPath = filemanPath.substring(0, filemanPath.length() - 1);
        filemanPath = URLDecoder.decode(filemanPath, "UTF-8");
        File file = new File(root, filemanPath);
        Collection<Part> parts = request.getParts();
        // 如果没有文件则返回
        if (parts.isEmpty()) {
            return;
        }
        // 如果所在目录不存在则创建
        File parent = file.getParentFile();
        if (!parent.exists() && !parent.mkdirs()) {
            response.sendError(HttpURLConnection.HTTP_BAD_REQUEST, "Bad Request");
            return;
        }
        // 如果当前路径是一个文件夹则冲突
        if (file.isDirectory()) {
            response.sendError(HttpURLConnection.HTTP_CONFLICT, "Conflict");
            return;
        }
        Part part = parts.iterator().next();
        part.write(file.getPath());
    }

    protected void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestPath = request.getRequestURI();
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String filemanPath = requestPath.substring(contextPath.length() + servletPath.length());
        while (filemanPath.endsWith("/")) filemanPath = filemanPath.substring(0, filemanPath.length() - 1);
        filemanPath = URLDecoder.decode(filemanPath, "UTF-8");
        File file = new File(root, filemanPath);
        boolean deleted = Toolkit.delete(file);
        if (!deleted) response.sendError(HttpURLConnection.HTTP_INTERNAL_ERROR);
    }

    protected void destroy() {
        Toolkit.release(configuration);
        Toolkit.release(synthesizer);
        Toolkit.release(formatter);
        for (Converter converter : converters) Toolkit.release(converter);
        for (Extractor extractor : extractors) Toolkit.release(extractor);
        for (Interceptor interceptor : interceptors) Toolkit.release(interceptor);
    }

    static class ConverterConfigFilter implements Filter {
        private final List<String> names = Arrays.asList(
                "converter.properties",
                "resolver.properties",
                "renderer.properties",
                "adapter.properties"
        );

        @Override
        public boolean accept(Resource resource, FilterChain chain) {
            return names.contains(resource.getName()) && chain.doNext(resource);
        }
    }

    static class ExtractorConfigFilter implements Filter {

        @Override
        public boolean accept(Resource resource, FilterChain chain) {
            return "extractor.properties".equals(resource.getName()) && chain.doNext(resource);
        }
    }

    static class InterceptorConfigFilter implements Filter {

        @Override
        public boolean accept(Resource resource, FilterChain chain) {
            return "interceptor.properties".equals(resource.getName()) && chain.doNext(resource);
        }
    }
}
