package io.fileman;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Iterator;

/**
 * 拦截上下文
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/25
 */
public class InterceptContext extends ActionContext {
    private final Iterator<Interceptor> interceptors;

    public InterceptContext(ActionContext context, Iterator<Interceptor> interceptors) {
        super(context);
        this.interceptors = interceptors;
    }

    public InterceptContext(File root, Configuration configuration, HttpServletRequest request, HttpServletResponse response, Iterator<Interceptor> interceptors) {
        super(root, configuration, request, response);
        this.interceptors = interceptors;
    }

    public void doNext(File file) throws Exception {
        if (interceptors.hasNext()) interceptors.next().intercept(file, this);
        else throw new IllegalStateException();
    }

}
