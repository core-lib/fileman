package io.fileman;

import java.io.File;
import java.io.IOException;

/**
 * 适配器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public abstract class Adapter implements Resolver, Renderer {

    @Override
    public String name() {
        return key();
    }

    @Override
    public String column() {
        return key();
    }

    @Override
    public Object render(File file, RenderContext context) throws IOException {
        return resolve(file, new ResolveContext(context));
    }

}
