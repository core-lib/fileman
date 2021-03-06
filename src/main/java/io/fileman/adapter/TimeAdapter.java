package io.fileman.adapter;

import io.fileman.Adapter;
import io.fileman.ResolveContext;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 最后更新日期适配器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public class TimeAdapter extends Adapter {

    @Override
    public String column() {
        return "Time";
    }

    @Override
    public String key() {
        return "time";
    }

    @Override
    public Object resolve(File file, ResolveContext context) {
        Date lastModified = new Date(file.lastModified());
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastModified);
    }
}
