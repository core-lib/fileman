package io.fileman.sorter;

import java.io.File;

/**
 * 体积排序器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/11 17:38
 */
public class SizeSorter extends AbstractSorter {

    @Override
    public int compare(File a, File b) {
        return Long.compare(a.length(), b.length());
    }
}
