package io.fileman.spring;

import io.fileman.FilemanServletSupport;
import io.fileman.Formatter;
import io.fileman.RenderSynthesizer;
import io.fileman.Synthesizer;
import io.fileman.formatter.HtmlFormatter;
import org.springframework.context.annotation.Import;

import javax.servlet.Servlet;
import java.lang.annotation.*;

/**
 * fileman 框架与 Spring Boot 集成注解
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-08-08 9:45
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import(FilemanServletRegistrar.class)
public @interface EnableFileman {

    /**
     * @return Fileman root folder
     */
    String root() default "";

    /**
     * @return Servlet name
     */
    String name() default "fileman";

    /**
     * @return Bean name
     */
    String bean() default "fileman";

    /**
     * @return URL mappings
     */
    String[] value() default {"/fileman/*"};

    /**
     * @return Filter Class
     */
    Class<? extends Servlet> servlet() default FilemanServletSupport.class;

    /**
     * @return Servlet Order
     */
    int order() default Integer.MAX_VALUE;

    /**
     * @return Servlet async supported
     */
    boolean asyncSupported() default true;

    /**
     * @return Servlet enabled
     */
    boolean enabled() default true;

    /**
     * @return load on startup
     */
    int loadOnStartup() default -1;

    /**
     * @return Download buffer size
     */
    int buffer() default 1024 * 8;

    /**
     * @return File fields
     */
    String[] fields() default {};

    /**
     * @return Download range units
     */
    String[] ranges() default {};

    /**
     * @return Interceptor names
     */
    String[] interceptors() default {"security"};

    /**
     * @return File properties synthesizer
     */
    Class<? extends Synthesizer> synthesizer() default RenderSynthesizer.class;

    /**
     * @return Data formatter
     */
    Class<? extends Formatter> formatter() default HtmlFormatter.class;

    /**
     * @return Servlet Init Parameters
     */
    Param[] params() default {};

    /**
     * @return Multipart config
     */
    Multipart multipart() default @Multipart(enabled = false);

}
