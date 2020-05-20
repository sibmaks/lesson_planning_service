package xyz.dma.soft.entity;

import xyz.dma.soft.utils.RequestLogUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BufferedRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] content;
    private String payload;

    public BufferedRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        int contentLength = request.getContentLength();
        ByteArrayOutputStream baos = new ByteArrayOutputStream(contentLength >= 0 ? contentLength : 1024);
        InputStream is = request.getInputStream();
        byte[] buffer = new byte[1024];
        int bytesCount;
        while ((bytesCount = is.read(buffer)) > 0) {
            baos.write(buffer, 0, bytesCount);
        }
        content = baos.toByteArray();
    }

    @Override
    public ServletInputStream getInputStream() {
        return new ServletInputStream() {
            private final ByteArrayInputStream stream = new ByteArrayInputStream(content);

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {
            }

            @Override
            public int read() {
                return stream.read();
            }
        };
    }

    /**
     * Формируем лог c телом запроса
     *
     * @return payload
     */
    public String extractPayload(String... keysToHide) {
        if (this.payload == null) {
            this.payload = RequestLogUtils.extractPayload(content, getCharacterEncoding(), keysToHide);
        }
        return payload;
    }
}
