package io.fileman;

/**
 * 转换器标记接口
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public interface Converter extends Plugin {

    /**
     * SPI 实现名称，用于和配置的值对应。
     *
     * @return SPI 实现名称
     */
    String name();

}
