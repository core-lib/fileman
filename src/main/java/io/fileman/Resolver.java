package io.fileman;

import java.io.File;
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
     * @param file    待解析文件
     * @param context 解析上下文
     * @return 解析结果
     * @throws IOException I/O 异常
     */
    Object resolve(File file, ResolveContext context) throws IOException;

}
