package io.fileman.formatter;

import io.fileman.Fileman;
import io.fileman.Filemans;
import io.fileman.Formatter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * HTML格式化器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/14
 */
public class HtmlFormatter implements Formatter {

    @Override
    public String getContentType() {
        return "text/html";
    }

    @Override
    public void format(Fileman fileman, OutputStream out) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(out);
        PrintWriter pw = new PrintWriter(osw);
        String path = Filemans.ifEmpty(fileman.getPath(), "/");

        pw.println("<html>");
        pw.println("<head>");
        pw.println("    <title>Index of " + path + "</title>");
        pw.println("    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>");
        pw.println("</head>");
        pw.println("<body>");
        pw.println("<h1>Index of " + path + "</h1>");
        pw.println("<table cellspacing=\"10\" align=\"left\">");
        pw.println("    <thead>");
        Set<String> columns = fileman.getProperties().keySet();
        int index = 0;
        for (String column : columns) {
            if (index++ == 0) pw.println("        <th align=\"left\">" + column + "</th>");
            else pw.println("        <th>" + column + "</th>");
        }
        pw.println("    </thead>");
        pw.println("    <tr>");
        pw.println("        <td>");

        pw.println("            <a href=\"\">Parent Directory</a>");
        pw.println("        </td>");
        List<Fileman> children = fileman.getChildren();
        for (Fileman child : children) {
            pw.println("    </tr>");
            Map<String, Object> properties = child.getProperties();
            for (String column : columns) {
                pw.println("        <td>" + properties.get(column) + "</td>");
            }
            pw.println("    </tr>");
        }
        pw.println("</table>");
        pw.println("</body>");
        pw.println("</html>");
        pw.flush();
        osw.flush();
    }
}
