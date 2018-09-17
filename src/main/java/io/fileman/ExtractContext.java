package io.fileman;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * 提取上下文
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/17
 */
public class ExtractContext extends ActionContext {

    public ExtractContext(ActionContext context) {
        super(context);
    }

    public ExtractContext(File root, Configuration configuration, HttpServletRequest request, HttpServletResponse response) {
        super(root, configuration, request, response);
    }

}
