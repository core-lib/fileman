package io.fileman;

import java.io.IOException;

/**
 * 格式化器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public interface Formatter extends Plugin {

    /**
     * 格式化输出
     *
     * @param fileman 文件目录
     * @param context 格式化上下文
     */
    void format(Fileman fileman, FormatContext context) throws IOException;

}
