package io.fileman.formatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.fileman.Fileman;
import io.fileman.FormatContext;
import io.fileman.Formatter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * JSON格式化器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public class JsonFormatter implements Formatter {
    private final ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();

    @Override
    public void format(Fileman fileman, FormatContext context) throws IOException {
        HttpServletResponse response = context.getResponse();
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        this.writer.writeValue(out, fileman);
    }

}
