package io.fileman.sorter;

import java.io.File;

/**
 * 路径排序器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/11 17:21
 */
public class PathSorter extends AbstractSorter {

    @Override
    public int compare(File a, File b) {
        return a.getPath().compareTo(b.getPath());
    }
}
