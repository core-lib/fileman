package io.fileman;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * 解析动作
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public abstract class ActionContext {
    /**
     * 文件管理器的管理根目录
     */
    protected final File root;
    /**
     * 框架集成配置
     */
    protected final Configuration configuration;
    /**
     * 请求
     */
    protected final HttpServletRequest request;
    /**
     * 回应
     */
    protected final HttpServletResponse response;

    protected ActionContext(ActionContext context) {
        this.root = context.root;
        this.configuration = context.configuration;
        this.request = context.request;
        this.response = context.response;
    }

    protected ActionContext(File root, Configuration configuration, HttpServletRequest request, HttpServletResponse response) {
        this.root = root;
        this.configuration = configuration;
        this.request = request;
        this.response = response;
    }

    public File getRoot() {
        return root;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }
}
