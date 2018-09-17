package io.fileman.adapter;

import io.fileman.Adapter;
import io.fileman.ResolveContext;

import java.io.File;

/**
 * 文件大小适配器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public class SizeAdapter extends Adapter {

    @Override
    public String name() {
        return "Size";
    }

    @Override
    public String key() {
        return "size";
    }

    @Override
    public Object resolve(File file, ResolveContext context) {
        if (file.isDirectory()) return 0L;
        else return file.length();
    }
}
