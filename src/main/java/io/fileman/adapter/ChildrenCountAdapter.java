package io.fileman.adapter;

import io.fileman.Action;
import io.fileman.Adapter;

import java.io.File;

/**
 * 子文件数量适配器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public class ChildrenCountAdapter extends Adapter {

    @Override
    public String name() {
        return "Children-Count";
    }

    @Override
    public String key() {
        return "childrenCount";
    }

    @Override
    public Object resolve(Action action) {
        File file = action.getFile();
        if (file.isFile()) return 0L;
        String[] list = file.list();
        return list != null ? list.length : 0L;
    }
}
