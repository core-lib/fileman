package io.fileman.interceptor;

import io.fileman.*;

import java.io.File;

/**
 * 安全认证拦截器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/25
 */
public class SecurityInterceptor implements Interceptor, Initialable {

    @Override
    public void initialize(Configuration configuration) {
        String location = configuration.valueOf("security-config-location");
        if (Filemans.isBlank(location)) location = "fileman-security.xml";
        String[] locations = location.split(SPLIT_DELIMIT_REGEX);

    }

    @Override
    public void intercept(File file, InterceptContext context) throws Exception {
        context.doNext(file);
    }

}
