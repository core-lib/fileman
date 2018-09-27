package io.fileman.spring;

import io.fileman.Toolkit;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import javax.servlet.MultipartConfigElement;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HttpDoc Filter 注册器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-08-08 9:54
 **/
public class FilemanServletRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableFileman.class.getName()));
        BeanDefinition fileman = new RootBeanDefinition(ServletRegistrationBean.class);

        String name = attributes.getString("name");
        fileman.getPropertyValues().add("name", name);

        int order = attributes.getNumber("order");
        fileman.getPropertyValues().add("order", order);

        String[] mappings = attributes.getStringArray("mappings");
        fileman.getPropertyValues().add("urlMappings", Arrays.asList(mappings));

        Class<?> servlet = attributes.getClass("servlet");
        fileman.getPropertyValues().add("servlet", newInstance(servlet));

        boolean async = attributes.getBoolean("async");
        fileman.getPropertyValues().add("asyncSupported", async);

        boolean enabled = attributes.getBoolean("enabled");
        fileman.getPropertyValues().add("enabled", enabled);

        int load = attributes.getNumber("load");
        fileman.getPropertyValues().add("loadOnStartup", load);

        AnnotationAttributes multipart = attributes.getAnnotation("multipart");
        if (multipart.getBoolean("enabled")) {
            MultipartConfigElement multipartConfig = new MultipartConfigElement(
                    multipart.getString("location"),
                    multipart.getNumber("maxFileSize").longValue(),
                    multipart.getNumber("maxRequestSize").longValue(),
                    multipart.getNumber("fileSizeThreshold").intValue()
            );
            fileman.getPropertyValues().add("multipartConfig", multipartConfig);
        }

        Map<String, String> parameters = new LinkedHashMap<>();
        AnnotationAttributes[] params = attributes.getAnnotationArray("params");
        for (AnnotationAttributes param : params) parameters.put(param.getString("name"), param.getString("value"));

        parameters.put("root", attributes.getString("value"));
        parameters.put("buffer", attributes.getNumber("buffer").toString());
        parameters.put("fields", Toolkit.join(attributes.getStringArray("fields"), ","));
        parameters.put("ranges", Toolkit.join(attributes.getStringArray("ranges"), ","));
        parameters.put("interceptors", Toolkit.join(attributes.getStringArray("interceptors"), ","));
        parameters.put("synthesizer", attributes.getClass("synthesizer").getName());
        parameters.put("formatter", attributes.getClass("formatter").getName());

        fileman.getPropertyValues().add("initParameters", parameters);

        String beanName = attributes.getString("bean");
        registry.registerBeanDefinition(beanName, fileman);
    }

    private <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
