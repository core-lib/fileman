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

    String location() default "";

    long maxFileSize() default -1L;

    long maxRequestSize() default -1L;

    int fileSizeThreshold() default -1;

}
