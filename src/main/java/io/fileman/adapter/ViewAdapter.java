package io.fileman.adapter;

import io.fileman.Adapter;
import io.fileman.RenderContext;
import io.fileman.ResolveContext;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * 查看适配器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/28
 */
public class ViewAdapter extends Adapter {

    @Override
    public String name() {
        return "View";
    }

    @Override
    public Object render(File file, RenderContext context) {
        if (file.isDirectory()) return "";
        return "<a href=\"" + resolve(file, new ResolveContext(context)) + "\" target=\"_blank\">" + key() + "</a>";
    }

    @Override
    public String key() {
        return "view";
    }

    @Override
    public Object resolve(File file, ResolveContext context) {
        if (file.isDirectory()) return "";
        HttpServletRequest request = context.getRequest();
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        File root = context.getRoot();
        String filemanPath = root.toURI().relativize(file.toURI()).toString();
        return ("/" + contextPath + "/fileman.html#" + "/" + contextPath + "/" + servletPath + "/" + filemanPath).replaceAll("/+", "/");
    }
}
