package io.fileman;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * 合成上下文
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public class SynthesizeContext<T extends Converter> extends ActionContext {
    private final List<T> converters;

    public SynthesizeContext(File root, Configuration configuration, HttpServletRequest request, HttpServletResponse response, List<T> converters) {
        super(root, configuration, request, response);
        this.converters = converters;
    }

    public List<T> getConverters() {
        return converters;
    }

}
