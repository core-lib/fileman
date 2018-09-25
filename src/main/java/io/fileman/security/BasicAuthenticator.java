package io.fileman.security;

import org.apache.commons.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Basic算法认证器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/25
 */
public class BasicAuthenticator implements Authenticator {

    @Override
    public String algorithm() {
        return "Basic";
    }

    @Override
    public void authenticate(File file, AuthenticateContext context) throws SecurityException {
        HttpServletRequest request = context.getRequest();
        String authorization = request.getHeader("Authorization");
        int index = authorization != null ? authorization.indexOf(' ') : -1;
        if (index < 0) throw new UnauthorizedSecurityException();
        String type = authorization.substring(0, index);
        if (!"Basic".equalsIgnoreCase(type)) throw new UnauthorizedSecurityException();
        String credentials = authorization.substring(index + 1);
        String decoded = new String(Base64.decodeBase64(credentials), StandardCharsets.UTF_8);
        int idx = decoded.indexOf(':');
        if (idx < 0) throw new UnauthorizedSecurityException();
        String username = decoded.substring(0, idx);
        String password = decoded.substring(idx + 1);
        Map<String, User> users = context.getUsers();
        User user = users.get(username);
        if (user == null || !password.equals(user.getPassword())) throw new UnauthorizedSecurityException();
        String method = request.getMethod();
        File root = context.getRoot();
        String path = root.toURI().relativize(file.toURI()).toString();
        if (path.isEmpty()) path = "/";
        if (!user.matches(method, path)) throw new ForbiddenSecurityException();
    }

}
