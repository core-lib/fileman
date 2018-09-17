package io.fileman.adapter;

import io.fileman.Adapter;
import io.fileman.RenderContext;
import io.fileman.ResolveContext;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * 名称适配器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public class NameAdapter extends Adapter {

    @Override
    public String name() {
        return "Name";
    }

    @Override
    public Object render(File file, RenderContext context) {
        Object name = resolve(file, new ResolveContext(context));
        HttpServletRequest request = context.getRequest();
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        File root = context.getRoot();
        String filemanPath = root.toURI().relativize(file.toURI()).toString();
        String URI = ("/" + contextPath + "/" + servletPath + "/" + filemanPath).replaceAll("/+", "/");
        return "<a href=\"" + URI + "\">" + name + "</a>";
    }

    @Override
    public String key() {
        return "name";
    }

    @Override
    public Object resolve(File file, ResolveContext context) {
        if (file.isDirectory()) return file.getName() + "/";
        else return file.getName();
    }
}
