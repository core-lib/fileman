package io.fileman;

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
    public Object render(Action action) throws IOException {
        return resolve(action);
    }

}
