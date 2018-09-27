package io.fileman.spring;

import java.lang.annotation.*;

/**
 * Filter Init Parameter
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-08-08 10:39
 **/
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Param {

    /**
     * @return Servlet Init Parameter Name
     */
    String name();

    /**
     * @return Servlet Init Parameter Value
     */
    String value();

}
