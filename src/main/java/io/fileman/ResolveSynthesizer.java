package io.fileman;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析合成器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public class ResolveSynthesizer implements Synthesizer<Resolver> {

    @Override
    public Map<String, Object> synthesize(File file, SynthesizeContext<Resolver> context) throws IOException {
        Map<String, Object> properties = new LinkedHashMap<String, Object>();
        List<Resolver> resolvers = context.getConverters();
        for (Resolver resolver : resolvers) {
            String name = resolver.key();
            Object value = resolver.resolve(file, new ResolveContext(context));
            properties.put(name, value);
        }
        return properties;
    }
}
