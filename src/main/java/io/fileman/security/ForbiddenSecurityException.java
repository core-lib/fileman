package io.fileman.security;

/**
 * 不允许访问异常
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/25
 */
public class ForbiddenSecurityException extends SecurityException {
    public ForbiddenSecurityException() {
    }

    public ForbiddenSecurityException(String message) {
        super(message);
    }

    public ForbiddenSecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForbiddenSecurityException(Throwable cause) {
        super(cause);
    }
}
