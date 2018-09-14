package io.fileman.adapter;

import io.fileman.Action;
import io.fileman.Adapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 最后更新日期适配器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public class LastModifiedAdapter extends Adapter {

    @Override
    public String name() {
        return "Last-Modified";
    }

    @Override
    public String key() {
        return "lastModified";
    }

    @Override
    public Object resolve(Action action) {
        File file = action.getFile();
        Date lastModified = new Date(file.lastModified());
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastModified);
    }
}
