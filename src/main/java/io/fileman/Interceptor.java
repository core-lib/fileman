package io.fileman;

import java.io.File;

/**
 * 拦截器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/25
 */
public interface Interceptor extends Plugin {

    /**
     * SPI 实现名称，用于和配置的值对应。
     *
     * @return SPI 实现名称
     */
    String name();

    /**
     * 拦截
     *
     * @param file    当前访问文件
     * @param context 拦截上下文
     * @throws Exception 拦截异常
     */
    void intercept(File file, InterceptContext context) throws Exception;

}
