package io.fileman.formatter;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import io.fileman.Fileman;
import io.fileman.Formatter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * JSON格式化器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public class YamlFormatter implements Formatter {
    private final ObjectWriter writer = new YAMLMapper().writerWithDefaultPrettyPrinter();

    @Override
    public void format(Fileman fileman, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/yaml");
        OutputStream out = response.getOutputStream();
        writer.writeValue(out, fileman);
    }

}
