package io.fileman.security;

import io.fileman.ActionContext;
import io.fileman.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Map;

/**
 * 认证上下文
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/25
 */
public class AuthenticateContext extends ActionContext {
    private final Map<String, User> users;

    public AuthenticateContext(ActionContext context, Map<String, User> users) {
        super(context);
        this.users = users;
    }

    public AuthenticateContext(File root, Configuration configuration, HttpServletRequest request, HttpServletResponse response, Map<String, User> users) {
        super(root, configuration, request, response);
        this.users = users;
    }

    public Map<String, User> getUsers() {
        return users;
    }
}
