package io.fileman;

import java.io.File;

/**
 * 排序器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/11 17:11
 */
public interface Sorter {

    /**
     * 排序
     *
     * @param files 文件列表
     */
    void sort(File[] files, boolean asc);

}
