package io.fileman.security;

import org.dom4j.Element;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 角色
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/25
 */
public class Role extends Node {
    private final String name;
    private final Set<Resource> resources;

    public Role(Element element) {
        super(element);
        this.name = element.attributeValue("name");
        this.resources = new LinkedHashSet<>();
    }

    @Override
    public boolean matches(String method, String path) {
        for (Resource resource : resources) if (resource.matches(method, path)) return true;
        return false;
    }

    public String getName() {
        return name;
    }

    public Set<Resource> getResources() {
        return resources;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        return name != null ? name.equals(role.name) : role.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public String toString() {
        return name;
    }
}
