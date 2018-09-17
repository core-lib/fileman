package io.fileman;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * 文件管理器工具类
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public abstract class Filemans {

    private Filemans() {
        throw new UnsupportedOperationException();
    }

    public static boolean isNull(Object value) {
        return value == null;
    }

    public static boolean isEmpty(String value) {
        return isNull(value) || value.isEmpty();
    }

    public static boolean isBlank(String value) {
        return isEmpty(value) || value.trim().isEmpty();
    }

    public static <T> T ifNull(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static String ifEmpty(String value, String defaultValue) {
        return value == null || value.isEmpty() ? defaultValue : value;
    }

    public static String ifBlank(String value, String defaultValue) {
        return value == null || value.trim().isEmpty() ? defaultValue : value;
    }

    public static <T> T newInstance(String className) {
        try {
            return (T) Class.forName(className).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void close(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException e) {
            // nothing to do...
        }
    }

    public static String join(Collection<?> collection, String delimit) {
        StringBuilder joined = new StringBuilder();
        Iterator<?> iterator = collection.iterator();
        while (iterator.hasNext()) {
            joined.append(iterator.next());
            if (iterator.hasNext()) joined.append(delimit);
        }
        return joined.toString();
    }
}
