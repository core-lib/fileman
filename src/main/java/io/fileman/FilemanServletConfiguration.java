package io.fileman;

import javax.servlet.ServletConfig;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Servlet配置
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public class FilemanServletConfiguration implements Configuration {
    private final ServletConfig servletConfig;
    private final Pattern pattern = Pattern.compile("\\$\\{(?<key>.*?)}");

    FilemanServletConfiguration(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    @Override
    public Enumeration<String> names() {
        return servletConfig.getInitParameterNames();
    }

    @Override
    public String valueOf(String name) {
        String value = servletConfig.getInitParameter(name);
        if (value == null) {
            return null;
        }
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
            String placeholder = matcher.group();
            String key = matcher.group("key").trim();
            String val = System.getProperty(key, System.getenv(key));
            value = value.replace(placeholder, String.valueOf(val));
        }
        return value;
    }

    @Override
    public String valueOf(String name, String defaultValue) {
        String value = valueOf(name);
        return value != null ? value : defaultValue;
    }

}
