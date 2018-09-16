package io.fileman.servlet;

import io.fileman.Configuration;

import javax.servlet.ServletConfig;
import java.util.Enumeration;

/**
 * Servlet配置
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public class ServletConfiguration implements Configuration {
    private final ServletConfig servletConfig;

    public ServletConfiguration(ServletConfig servletConfig) {
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

}
