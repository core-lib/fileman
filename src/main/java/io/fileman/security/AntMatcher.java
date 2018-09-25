package io.fileman.security;

/**
 * ant风格表达式匹配器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/25
 */
public class AntMatcher implements Matcher {

    /**
     * 文本与ant表达式匹配
     * 情况：
     * 1. 路径中没有层级通配符
     * 2. 路径中包含层级通配符
     * 2.1. 层级通配符在最前面
     * 2.2. 层级通配符在中间
     *
     * @param text    文本
     * @param pattern 表达式
     * @return
     */
    @Override
    public boolean matches(String text, String pattern) {
        String[] segments = pattern.split("/");

        return false;
    }

}
