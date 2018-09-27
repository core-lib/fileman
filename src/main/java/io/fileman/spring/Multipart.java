package io.fileman.spring;

import java.lang.annotation.*;

/**
 * Servlet multipart-config 标签注解
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/26
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Multipart {

    /**
     * Whether multipart/form-data is accepted
     */
    boolean enabled() default true;

    /**
     * The directory location where files will be stored
     */
    String location() default "";

    /**
     * The maximum size allowed for uploaded files.
     *
     * <p>The default is <tt>-1L</tt>, which means unlimited.
     */
    long maxFileSize() default -1L;

    /**
     * The maximum size allowed for <tt>multipart/form-data</tt>
     * requests
     *
     * <p>The default is <tt>-1L</tt>, which means unlimited.
     */
    long maxRequestSize() default -1L;

    /**
     * The size threshold after which the file will be written to disk
     */
    int fileSizeThreshold() default 0;

}
