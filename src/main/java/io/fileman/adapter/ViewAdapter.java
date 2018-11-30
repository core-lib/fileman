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
    public String column() {
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
        File root = context.getRoot();
        String filemanPath = root.toURI().relativize(file.toURI()).toString();
        int length = filemanPath.split("/+").length;
        StringBuilder path = new StringBuilder();
        for (int i = 0; i < length; i++) {
            path.append("../");
        }
        path.append("fileman.html");
        HttpServletRequest request = context.getRequest();
        String servletPath = request.getServletPath();
        String hash = "./" + servletPath + "/" + filemanPath;
        return (path + "#" + hash).replaceAll("/+", "/");
    }
}
