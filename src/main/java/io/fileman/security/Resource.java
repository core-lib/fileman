package io.fileman.security;

import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 资源
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/25
 */
public class Resource extends Node {
    private static final String[] SYMBOLS = {"\\", "$", "(", ")", "+", ".", "[", "]", "^", "{", "}", "|"};
    private final String name;
    private final List<String> methods;
    private final List<Pattern> patterns;

    public Resource(Element element) {
        super(element);

        this.name = element.attributeValue("name");

        String method = element.attributeValue("method");
        this.methods = method == null || method.isEmpty() || method.equals("*")
                ? Collections.<String>emptyList()
                : Arrays.asList(method.toUpperCase().split("\\s*,\\s*"));

        String path = element.attributeValue("path");
        this.patterns = new ArrayList<>();
        String[] expressions = path.split("\\s*,\\s*");
        for (String expression : expressions) {
            for (String symbol : SYMBOLS) expression = expression.replace(symbol, '\\' + symbol);
            expression = expression.replace("**", ".*");
            expression = expression.replace("*", "[^/]*");
            expression = expression.replace("?", ".{1}");
            patterns.add(Pattern.compile(expression));
        }
    }

    @Override
    public boolean matches(String method, String path) {
        if (!methods.isEmpty() && !methods.contains(method.toUpperCase())) return false;
        for (Pattern pattern : patterns) if (pattern.matcher(path).matches()) return true;
        return false;
    }

    public String getName() {
        return name;
    }

    public List<String> getMethods() {
        return methods;
    }

    public List<Pattern> getPatterns() {
        return patterns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resource resource = (Resource) o;

        return name != null ? name.equals(resource.name) : resource.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public String toString() {
        return name;
    }
}
