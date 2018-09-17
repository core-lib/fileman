package io.fileman;

import javax.servlet.FilterConfig;
import java.util.Enumeration;

/**
 * 文件管理器Filter配置
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/17
 */
public class FilemanFilterConfiguration implements Configuration {
    private final FilterConfig filterConfig;

    FilemanFilterConfiguration(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    @Override
    public Enumeration<String> names() {
        return filterConfig.getInitParameterNames();
    }

    @Override
    public String valueOf(String name) {
        return filterConfig.getInitParameter(name);
    }
}
