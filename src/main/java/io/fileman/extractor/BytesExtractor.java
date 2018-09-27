package io.fileman.extractor;

import io.fileman.ExtractContext;
import io.fileman.Extractor;
import io.fileman.Range;
import io.fileman.Toolkit;

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
    public String unit() {
        return "bytes";
    }

    @Override
    public void extract(File file, Range range, ExtractContext context) throws IOException {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rws");
            long start = range.getStart();
            long end = range.getEnd();
            long total = raf.length();
            long first = Math.abs(start);
            long last = Math.min(end, total - 1);
            long length = last - first + 1;
            HttpServletResponse response = context.getResponse();
            if (length <= 0) {
                response.sendError(HttpURLConnection.HTTP_NO_CONTENT, "No Content");
                return;
            }
            response.setStatus(HttpURLConnection.HTTP_PARTIAL);
            response.setHeader("Content-Range", "bytes " + first + "-" + last + "/" + total);
            OutputStream out = response.getOutputStream();
            raf.seek(first);
            byte[] buf = new byte[1024 * 8];
            while (length > 0) {
                int len = raf.read(buf, 0, (int) Math.min(buf.length, length));
                if (len < 0) break;
                out.write(buf, 0, len);
                length -= len;
            }
        } finally {
            Toolkit.close(raf);
        }
    }

}
