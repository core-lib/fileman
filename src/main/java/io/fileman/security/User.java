package io.fileman.security;

import org.dom4j.Element;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 用户
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/25
 */
public class User extends Node {
    private final String name;
    private final String password;
    private final Set<Role> roles;

    public User(Element element) {
        super(element);
        this.name = element.attributeValue("name");
        this.password = element.attributeValue("password");
        this.roles = new LinkedHashSet<>();
    }

    @Override
    public boolean matches(String method, String path) {
        for (Role role : roles) if (role.matches(method, path)) return true;
        return false;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return name != null ? name.equals(user.name) : user.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return name;
    }
}
