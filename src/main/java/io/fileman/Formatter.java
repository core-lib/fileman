package io.fileman;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 格式化器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public interface Formatter {

    /**
     * @return 内容类型
     */
    String getContentType();

    /**
     * 格式化输出
     *
     * @param fileman 文件目录
     * @param out     输出流
     */
    void format(Fileman fileman, OutputStream out) throws IOException;

}
