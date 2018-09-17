package io.fileman;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;

/**
 * 字节范围文件内容提取器
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/9/17
 */
public class BytesExtractor implements Extractor {

    @Override
    public boolean supports(File file, Range range) {
        return "bytes".equalsIgnoreCase(range.getUnit());
    }

    @Override
    public void extract(File file, Range range, ExtractContext context) throws IOException {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rws");
            long start = range.getStart();
            long end = range.getEnd() > 0 ? range.getEnd() : Long.MAX_VALUE;
            long total = raf.length();
            long length = Math.min(end, total - 1) - start;
            HttpServletResponse response = context.getResponse();
            if (length <= 0) {
                response.sendError(HttpURLConnection.HTTP_NO_CONTENT, "No Content");
                return;
            }
            response.setStatus(HttpURLConnection.HTTP_PARTIAL);
            response.setHeader("Content-Range", "bytes " + start + "-" + Math.min(end, total - 1) + "/" + total);
            OutputStream out = response.getOutputStream();
            raf.seek(start);
            byte[] buf = new byte[1024 * 8];
            while (length > 0) {
                int len = raf.read(buf, 0, (int) Math.min(buf.length, length));
                if (len < 0) break;
                out.write(buf, 0, len);
                length -= len;
            }
        } finally {
            Filemans.close(raf);
        }
    }

}
