package io.fileman;

import java.util.Enumeration;

/**
 * 配置
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public interface Configuration {

    /**
     * @return 所有配置项的名称
     */
    Enumeration<String> names();

    /**
     * 获取指定配置项名称的配置值
     *
     * @param name 配置项名称
     * @return 配置值
     */
    String valueOf(String name);

    /**
     * 获取指定配置项名称的配置值
     *
     * @param name         配置项名称
     * @param defaultValue 缺省值
     * @return 配置值
     */
    String valueOf(String name, String defaultValue);

}
