package io.fileman.sorter;

import java.io.File;

/**
 * 创建时间排序器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/11 17:23
 */
public class TimeSorter extends AbstractSorter {

    @Override
    public int compare(File a, File b) {
        return Long.compare(a.lastModified(), b.lastModified());
    }
}
