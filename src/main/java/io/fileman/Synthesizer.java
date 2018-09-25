package io.fileman;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * 合成器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public interface Synthesizer<T extends Converter> extends Plugin {

    /**
     * 文件信息合成
     *
     * @param file    信息合成文件
     * @param context 合成动作
     * @return 文件信息
     * @throws IOException I/O 异常
     */
    Map<String, Object> synthesize(File file, SynthesizeContext<T> context) throws IOException;

}
