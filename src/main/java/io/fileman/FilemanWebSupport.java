package io.fileman;

import io.fileman.formatter.HtmlFormatter;
import io.fileman.sorter.PathSorter;

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
    protected Sorter sorter;
    protected boolean asc;

    protected void init(Configuration configuration) throws ServletException {
        this.configuration = configuration;
        root = Toolkit.ifBlank(configuration.valueOf("root"), System.getProperty("user.dir"));
        synthesizer = Toolkit.newInstance(Toolkit.ifBlank(configuration.valueOf("synthesizer"), RenderSynthesizer.class.getName()));
        formatter = Toolkit.newInstance(Toolkit.ifBlank(configuration.valueOf("formatter"), HtmlFormatter.class.getName()));
        buffer = Integer.valueOf(Toolkit.ifBlank(configuration.valueOf("buffer"), "" + 1024 * 8));
        sorter = Toolkit.newInstance(Toolkit.ifBlank(configuration.valueOf("sorter"), PathSorter.class.getName()));
        asc = Boolean.valueOf(Toolkit.ifBlank(configuration.valueOf("asc"), "true"));
        initConverters(configuration);
        initExtractors(configuration);
        initInterceptors(configuration);
    }

    private void initConverters(Configuration configuration) throws ServletException {
        converters.clear();
        Map<String, Converter> map = new LinkedHashMap<>();
        for (Converter converter : ServiceLoader.load(Converter.class)) {
            map.put(converter.name(), converter);
        }
        for (Resolver resolver : ServiceLoader.load(Resolver.class)) {
            map.put(resolver.name(), resolver);
        }
        for (Renderer renderer : ServiceLoader.load(Renderer.class)) {
            map.put(renderer.name(), renderer);
        }
        String value = configuration.valueOf("fields");
        Iterable<String> names = Toolkit.isBlank(value) ? map.keySet() : Arrays.asList(value.split(SPLIT_DELIMIT_REGEX));
        for (String name : names) {
            try {
                Converter converter = map.get(name);
                if (converter instanceof Initialable) ((Initialable) converter).initialize(configuration);
                converters.add(converter);
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }
    }

    private void initExtractors(Configuration configuration) throws ServletException {
        extractors.clear();
        Map<String, Extractor> map = new LinkedHashMap<>();
        for (Extractor extractor : ServiceLoader.load(Extractor.class)) {
            map.put(extractor.unit(), extractor);
        }
        String value = configuration.valueOf("ranges");
        Iterable<String> names = Toolkit.isBlank(value) ? map.keySet() : Arrays.asList(value.split(SPLIT_DELIMIT_REGEX));
        for (String name : names) {
            try {
                Extractor extractor = map.get(name);
                if (extractor instanceof Initialable) ((Initialable) extractor).initialize(configuration);
                extractors.add(extractor);
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }
    }

    private void initInterceptors(Configuration configuration) throws ServletException {
        interceptors.clear();
        Map<String, Interceptor> map = new LinkedHashMap<>();
        for (Interceptor interceptor : ServiceLoader.load(Interceptor.class)) {
            map.put(interceptor.name(), interceptor);
        }
        String value = configuration.valueOf("interceptors");
        Iterable<String> names = Toolkit.isBlank(value) ? Collections.<String>emptyList() : Arrays.asList(value.split(SPLIT_DELIMIT_REGEX));
        for (String name : names) {
            try {
                Interceptor interceptor = map.get(name);
                if (interceptor instanceof Initialable) ((Initialable) interceptor).initialize(configuration);
                this.interceptors.add(interceptor);
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }
        this.interceptors.add(this);
    }

    protected void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
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
    public String name() {
        return "fileman";
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

            // 排序
            sorter.sort(files, asc);

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
                long size = file.length();
                String contentDisposition = "attachment; filename=\"" + URLEncoder.encode(name, "UTF-8") + "\"";
                response.setHeader("Content-Disposition", contentDisposition);
                response.setHeader("Content-Length", String.valueOf(size));

                OutputStream out = response.getOutputStream();
                InputStream in = null;
                try {
                    in = new FileInputStream(file);
                    byte[] buf = new byte[buffer];
                    int len;
                    long rem = size;
                    while ((len = in.read(buf, 0, rem > buf.length ? buf.length : (int) rem)) != -1) {
                        out.write(buf, 0, len);
                        rem = rem - len;
                        if (rem <= 0) break;
                    }
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

}
