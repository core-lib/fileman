package io.fileman;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 文件管理器Filter集成
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/17
 */
public class FilemanFilterSupport extends FilemanWebSupport implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(new FilemanFilterConfiguration(filterConfig));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
        super.handle((HttpServletRequest) request, (HttpServletResponse) request);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
