package io.fileman.adapter;

import io.fileman.Action;
import io.fileman.Adapter;

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
        File root = action.getRoot();
        File file = action.getFile();
        String path = file.getPath().substring(root.getPath().length()).replace(File.separator, "/");
        return "<a href=\"" + path + "\">" + name + "</a>";
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
