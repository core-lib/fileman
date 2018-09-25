package io.fileman;

/**
 * 可初始化的
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/25
 */
public interface Initialable extends Plugin {

    /**
     * 初始化
     *
     * @param configuration 配置
     */
    void initialize(Configuration configuration) throws Exception;

}
