package io.fileman;

/**
 * 可释放的
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/25
 */
public interface Releasable extends Plugin {

    /**
     * 释放
     */
    void release();

}
