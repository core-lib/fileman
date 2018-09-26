package io.fileman;

import java.io.File;
import java.io.IOException;

/**
 * 文件内容提取器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/17
 */
public interface Extractor extends Plugin {

    /**
     * 获取该提取器的单位名称
     *
     * @return 单位名称
     */
    String unit();

    /**
     * 提取指定文件的指定内容范围
     *
     * @param file    文件
     * @param range   内容范围
     * @param context 提取上下文
     * @throws IOException I/O 异常
     */
    void extract(File file, Range range, ExtractContext context) throws IOException;

}
