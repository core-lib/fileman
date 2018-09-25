package io.fileman.security;

import java.io.File;

/**
 * 认证器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/25
 */
public interface Authenticator {

    /**
     * 认证算法
     *
     * @return 认证算法
     */
    String algorithm();

    /**
     * 认证
     *
     * @param file    文件
     * @param context 认证上下文
     * @throws SecurityException 安全认证异常
     * @see UnauthorizedSecurityException 未认证异常
     * @see ForbiddenSecurityException 禁止访问异常
     */
    void authenticate(File file, AuthenticateContext context) throws SecurityException;

}
