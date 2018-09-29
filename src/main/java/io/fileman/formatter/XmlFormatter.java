package io.fileman.formatter;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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
public class XmlFormatter implements Formatter {
    private final ObjectWriter writer = new XmlMapper().writerWithDefaultPrettyPrinter();

    @Override
    public void format(Fileman fileman, FormatContext context) throws IOException {
        HttpServletResponse response = context.getResponse();
        response.setContentType("application/xml");
        PrintWriter out = response.getWriter();
        writer.writeValue(out, fileman);
    }

}
