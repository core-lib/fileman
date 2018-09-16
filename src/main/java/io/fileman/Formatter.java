package io.fileman;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 格式化器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public interface Formatter {

    /**
     * 格式化输出
     *
     * @param fileman  文件目录
     * @param request  请求
     * @param response 回应
     */
    void format(Fileman fileman, HttpServletRequest request, HttpServletResponse response) throws IOException;

}
