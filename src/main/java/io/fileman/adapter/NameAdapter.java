package io.fileman.adapter;

import io.fileman.Action;
import io.fileman.Adapter;

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
    public Object render(Action action) {
        Object name = resolve(action);
        HttpServletRequest request = action.getRequest();
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        File root = action.getRoot();
        File file = action.getFile();
        String filemanPath = root.toURI().relativize(file.toURI()).toString();
        String URI = ("/" + contextPath + "/" + servletPath + "/" + filemanPath).replaceAll("/+", "/");
        return "<a href=\"" + URI + "\">" + name + "</a>";
    }

    @Override
    public String key() {
        return "name";
    }

    @Override
    public Object resolve(Action action) {
        File file = action.getFile();
        if (file.isDirectory()) return file.getName() + "/";
        else return file.getName();
    }
}
