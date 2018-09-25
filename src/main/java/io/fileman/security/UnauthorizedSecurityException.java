package io.fileman.security;

/**
 * 未认证异常
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/25
 */
public class UnauthorizedSecurityException extends SecurityException {
    private static final long serialVersionUID = -4705920414901282544L;

    public UnauthorizedSecurityException() {
    }

    public UnauthorizedSecurityException(String message) {
        super(message);
    }

    public UnauthorizedSecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthorizedSecurityException(Throwable cause) {
        super(cause);
    }
}
