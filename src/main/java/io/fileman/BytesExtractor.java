package io.fileman;

import java.io.File;
import java.io.IOException;

/**
 * 字节范围文件内容提取器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/17
 */
public class BytesExtractor implements Extractor {

    @Override
    public boolean supports(File file, Range range) {
        return "bytes".equalsIgnoreCase(range.getUnit());
    }

    @Override
    public void extract(File file, Range range, ExtractContext context) throws IOException {

    }

}
