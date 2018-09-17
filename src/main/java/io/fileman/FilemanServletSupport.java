package io.fileman;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 文件管理Servlet集成
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/17
 */
public class FilemanServletSupport extends FilemanWebSupport implements Servlet {
    private ServletConfig servletConfig;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        this.servletConfig = servletConfig;
        super.init(new FilemanServletConfiguration(servletConfig));
    }

    @Override
    public ServletConfig getServletConfig() {
        return this.servletConfig;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws IOException, ServletException {
        super.handle((HttpServletRequest) req, (HttpServletResponse) res);
    }

    @Override
    public String getServletInfo() {
        return this.servletConfig.getServletName();
    }

    @Override
    public void destroy() {
        this.servletConfig = null;
        super.destroy();
    }
}
