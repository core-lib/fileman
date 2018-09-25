package io.fileman.security;

/**
 * 匹配器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/25
 */
public interface Matcher {

    /**
     * 匹配
     *
     * @param text    文本
     * @param pattern 表达式
     * @return 如果匹配则返回true 否则返回false
     */
    boolean matches(String text, String pattern);

}
