package io.fileman.security;

import io.fileman.FilemanException;

/**
 * 安全异常
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/25
 */
public class SecurityException extends FilemanException {

    public SecurityException() {
    }

    public SecurityException(String message) {
        super(message);
    }

    public SecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    public SecurityException(Throwable cause) {
        super(cause);
    }

}
