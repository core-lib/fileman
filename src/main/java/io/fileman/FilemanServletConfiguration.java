package io.fileman;

import javax.servlet.ServletConfig;
import java.util.Enumeration;

/**
 * Servlet配置
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public class FilemanServletConfiguration implements Configuration {
    private final ServletConfig servletConfig;

    FilemanServletConfiguration(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    @Override
    public Enumeration<String> names() {
        return servletConfig.getInitParameterNames();
    }

    @Override
    public String valueOf(String name) {
        return servletConfig.getInitParameter(name);
    }

    @Override
    public String valueOf(String name, String defaultValue) {
        String value = valueOf(name);
        return value != null ? value : defaultValue;
    }

}
