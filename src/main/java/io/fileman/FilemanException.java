package io.fileman;

/**
 * 文件管理器异常
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/25
 */
public class FilemanException extends Exception {
    private static final long serialVersionUID = -1184688801507341907L;

    public FilemanException() {
    }

    public FilemanException(String message) {
        super(message);
    }

    public FilemanException(String message, Throwable cause) {
        super(message, cause);
    }

    public FilemanException(Throwable cause) {
        super(cause);
    }

}
