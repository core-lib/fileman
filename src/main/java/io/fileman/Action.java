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
public class Action {
    /**
     * 框架集成配置
     */
    private final Configuration configuration;
    /**
     * 请求
     */
    private final HttpServletRequest request;
    /**
     * 回应
     */
    private final HttpServletResponse response;
    /**
     * 文件管理器的管理根目录
     */
    private final File root;
    /**
     * 当前需要解析的文件或目录
     */
    private final File file;

    Action(Configuration configuration, HttpServletRequest request, HttpServletResponse response, File root, File file) {
        this.configuration = configuration;
        this.request = request;
        this.response = response;
        this.root = root;
        this.file = file;
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

    public File getRoot() {
        return root;
    }

    public File getFile() {
        return file;
    }

    @Override
    public String toString() {
        return file.getAbsolutePath().substring(root.getAbsolutePath().length());
    }
}
