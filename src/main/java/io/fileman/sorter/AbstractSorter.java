package io.fileman.sorter;

import io.fileman.Sorter;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 抽象排序器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/11 17:32
 */
public abstract class AbstractSorter implements Sorter, Comparator<File> {

    @Override
    public void sort(File[] files, boolean asc) {
        Arrays.sort(files, asc ? this : new ReversedComparator<>(this));
    }

    /**
     * 反向对比器
     *
     * @author Payne 646742615@qq.com
     * 2019/5/11 17:30
     */
    static class ReversedComparator<T> implements Comparator<T> {
        private final Comparator<T> comparator;

        ReversedComparator(Comparator<T> comparator) {
            this.comparator = comparator;
        }

        @Override
        public int compare(T a, T b) {
            return -comparator.compare(a, b);
        }
    }
}
