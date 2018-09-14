package io.fileman;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析合成器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public class ResolvingSynthesizer implements Synthesizer<Resolver> {

    @Override
    public Map<String, Object> synthesize(Synthesization<Resolver> synthesization) {
        Map<String, Object> properties = new LinkedHashMap<String, Object>();
        List<Resolver> resolvers = synthesization.getConverters();
        for (Resolver resolver : resolvers) {
            String name = resolver.key();
            Object value = resolver.resolve(synthesization);
            properties.put(name, value);
        }
        return properties;
    }
}
