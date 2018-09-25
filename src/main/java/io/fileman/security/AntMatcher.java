package io.fileman.security;

/**
 * ant风格表达式匹配器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/25
 */
public class AntMatcher implements Matcher {

    // /**/abc/def/**/*.jsp
    @Override
    public boolean matches(String text, String pattern) {
        String[] segments = pattern.split("/");

        return false;
    }

}
