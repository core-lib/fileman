package io.fileman;

import java.io.File;
import java.io.IOException;

/**
 * 文件内容提取器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/17
 */
public interface Extractor {

    /**
     * 判断是否支持指定文件的指定范围内容提取
     *
     * @param file  文件
     * @param range 内容范围
     * @return 如果支持则返回true，否则返回false
     */
    boolean supports(File file, Range range);

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
