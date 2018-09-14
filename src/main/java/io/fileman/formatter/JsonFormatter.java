package io.fileman.formatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.fileman.Fileman;
import io.fileman.Formatter;

import java.io.IOException;
import java.io.OutputStream;

/**
 * JSON格式化器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public class JsonFormatter implements Formatter {
    private final ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();

    @Override
    public String getContentType() {
        return "application/json";
    }

    @Override
    public void format(Fileman fileman, OutputStream out) throws IOException {
        writer.writeValue(out, fileman);
    }

}
