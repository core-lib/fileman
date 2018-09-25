package io.fileman.security;

import org.dom4j.Element;

/**
 * 配置的
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/25
 */
public abstract class Node {
    protected final Element element;

    protected Node(Element element) {
        this.element = element;
    }

    public abstract boolean matches(String method, String path);

    public Element getElement() {
        return element;
    }
}
