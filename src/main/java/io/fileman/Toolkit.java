package io.fileman;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * 文件管理器工具类
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public abstract class Toolkit {

    private Toolkit() {
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

    public static String join(Object[] array, String delimit) {
        return join(Arrays.asList(array), delimit);
    }

    public static String quote(String value) {
        return value.startsWith("\"") && value.endsWith("\"") ? value : "\"" + value + "\"";
    }

    public static String unquote(String value) {
        return value.startsWith("\"") && value.endsWith("\"") ? value.substring(1, value.length() - 1) : value;
    }

    public static boolean delete(File file) {
        if (!file.exists()) return true;
        if (file.isFile()) return file.delete();
        File[] files = file.listFiles();
        boolean deleted = true;
        for (int i = 0; files != null && i < files.length; i++) {
            deleted = deleted && delete(files[i]);
        }
        return file.delete() && deleted;
    }

    public static void release(Object bean) {
        try {
            if (bean instanceof Releasable) ((Releasable) bean).release();
        } catch (Exception e) {
            // do nothing
        }
    }

    public static int lines(String filepath) throws IOException {
        return lines(new File(filepath));
    }

    public static int lines(File file) throws IOException {
        FileReader fr = null;
        LineNumberReader lnr = null;
        try {
            fr = new FileReader(file);
            lnr = new LineNumberReader(fr);
            while (lnr.readLine() != null) lnr.getLineNumber();
            return lnr.getLineNumber();
        } finally {
            close(lnr);
            close(fr);
        }
    }

    public static String charsetOf(File file) throws IOException {
        String charset = "GBK";
        byte[] first = new byte[3];
        boolean checked = false;
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            bis.mark(0);
            int read = bis.read(first, 0, 3);
            if (read == -1) return charset;
            if (first[0] == (byte) 0xFF && first[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
                checked = true;
            } else if (first[0] == (byte) 0xFE && first[1] == (byte) 0xFF) {
                charset = "UTF-16BE";
                checked = true;
            } else if (first[0] == (byte) 0xEF && first[1] == (byte) 0xBB && first[2] == (byte) 0xBF) {
                charset = "UTF-8";
                checked = true;
            }
            bis.reset();
            if (!checked) {
                while ((read = bis.read()) != -1) {
                    if (read >= 0xF0) break;
                    if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) continue;// 双字节 (0xC0 - 0xDF) (0x80  - 0xBF),也可能在GB编码内
                        else break;
                    } else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else break;
                        } else break;
                    }
                }
            }
        } finally {
            close(bis);
            close(fis);
        }
        return charset;
    }

}
