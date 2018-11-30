package io.fileman.adapter;

import io.fileman.Adapter;
import io.fileman.RenderContext;
import io.fileman.ResolveContext;

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
        return "<a href=\"./" + name + "\">" + name + "</a>";
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
