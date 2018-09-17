package io.fileman.adapter;

import io.fileman.Adapter;
import io.fileman.ResolveContext;

import java.io.File;

/**
 * ETag适配器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/17
 */
public class ETagAdapter extends Adapter {

    @Override
    public String name() {
        return "ETag";
    }

    @Override
    public String key() {
        return "etag";
    }

    @Override
    public Object resolve(File file, ResolveContext context) {
        return "W/\"" + file.length() + "-" + file.lastModified() + "\"";
    }
}
