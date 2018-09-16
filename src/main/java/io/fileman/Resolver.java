package io.fileman;

import java.io.IOException;

/**
 * 文件信息解析器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public interface Resolver extends Converter {

    /**
     * @return 字段名称
     */
    String key();

    /**
     * 解析
     *
     * @param action 解析动作
     * @return 解析结果
     * @throws IOException I/O 异常
     */
    Object resolve(Action action) throws IOException;

}
