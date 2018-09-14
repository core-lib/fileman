package io.fileman;

import java.util.Map;

/**
 * 合成器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public interface Synthesizer<T extends Converter> {

    /**
     * 文件信息合成
     *
     * @param synthesization 合成动作
     * @return 文件信息
     */
    Map<String, Object> synthesize(Synthesization<T> synthesization);

}
