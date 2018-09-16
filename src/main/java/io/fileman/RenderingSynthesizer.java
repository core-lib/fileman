package io.fileman;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 渲染合成器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public class RenderingSynthesizer implements Synthesizer<Renderer> {

    @Override
    public Map<String, Object> synthesize(Synthesization<Renderer> synthesization) throws IOException {
        Map<String, Object> properties = new LinkedHashMap<String, Object>();
        List<Renderer> renderers = synthesization.getConverters();
        for (Renderer renderer : renderers) {
            String name = renderer.name();
            Object value = renderer.render(synthesization);
            properties.put(name, value);
        }
        return properties;
    }
}
