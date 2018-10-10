package io.fileman.extractor;

import io.fileman.ExtractContext;
import io.fileman.Extractor;
import io.fileman.Range;
import io.fileman.Toolkit;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 按行内容提取器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/27
 */
public class LinesExtractor implements Extractor {

    @Override
    public String unit() {
        return "lines";
    }

    @Override
    public void extract(File file, Range range, ExtractContext context) throws IOException {
        long start = range.getStart();
        long end = range.getEnd();
        long total = Toolkit.lines(file);
        long first = start < 0 ? total < end ? 0 : total - end : start;
        long last = start < 0 ? total - 1 : Math.min(end, total - 1);
        HttpServletResponse response = context.getResponse();
        if (first > last) {
            response.sendError(HttpURLConnection.HTTP_NO_CONTENT, "No Content");
            return;
        }
        response.setStatus(HttpURLConnection.HTTP_PARTIAL);
        response.setHeader("Content-Range", "lines " + first + "-" + last + "/" + total);
        Path path = Paths.get(file.toURI());
        String contentType = Files.probeContentType(path);
        if (contentType == null) contentType = "text/plain";
        response.setContentType(contentType);

        PrintWriter writer = response.getWriter();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        LineNumberReader lnr = null;
        try {
            String charset = Toolkit.charsetOf(file);
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis, charset != null ? charset : Charset.defaultCharset().name());
            lnr = new LineNumberReader(isr);
            String line;
            int index = 0;
            while ((line = lnr.readLine()) != null) {
                if (index++ < first) continue;
                writer.println(line);
                if (index > last) break;
            }
            writer.flush();
        } finally {
            Toolkit.close(lnr);
            Toolkit.close(isr);
            Toolkit.close(fis);
        }
    }

}
