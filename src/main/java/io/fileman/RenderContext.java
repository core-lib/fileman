package io.fileman;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * 渲染上下文
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/17
 */
public class RenderContext extends ActionContext {

    public RenderContext(ActionContext context) {
        super(context);
    }

    public RenderContext(File root, Configuration configuration, HttpServletRequest request, HttpServletResponse response) {
        super(root, configuration, request, response);
    }

}
