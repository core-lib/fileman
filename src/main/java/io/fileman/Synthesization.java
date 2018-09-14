package io.fileman;

import java.io.File;
import java.util.List;

/**
 * 合成动作
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public class Synthesization<T extends Converter> extends Action {
    private final List<T> converters;

    public Synthesization(Configuration configuration, File root, File file, List<T> converters) {
        super(configuration, root, file);
        this.converters = converters;
    }

    public List<T> getConverters() {
        return converters;
    }

}
