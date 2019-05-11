package io.fileman.sorter;

import io.fileman.Sorter;

import java.io.File;

/**
 * 缺省排序，即文件系统排序
 *
 * @author Payne 646742615@qq.com
 * 2019/5/11 17:14
 */
public class DefaultSorter implements Sorter {

    @Override
    public void sort(File[] files, boolean asc) {
        // ignored
    }
}
