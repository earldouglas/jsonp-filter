package com.earldouglas.jsonpfilter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class JsonPFilter implements Filter {

    private String callbackParam = "callback";

    @Override public void init(FilterConfig config) throws ServletException {
        String _callbackParam = config.getInitParameter("callbackParam");
        if (_callbackParam != null) {
            callbackParam = _callbackParam.replaceAll("[\r\n\\s]", "");
        }
    }

    @Override public void destroy() {}

    @Override public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        String callback = null;

        if (req instanceof HttpServletRequest) {
            HttpServletRequest hreq = (HttpServletRequest) req;
            callback = hreq.getParameter(callbackParam);
        }

        if (callback != null) {
            OutputStream out = res.getOutputStream();
            out.write(new String(callback + "(").getBytes());
            chain.doFilter(req, res);
            out.write(new JsonPResponseWrapper((HttpServletResponse) res).getData());
            out.write(new String(")").getBytes());
            out.close();
        } else {
            chain.doFilter(req, res);
        }
    }

    private static class JsonPResponseWrapper extends HttpServletResponseWrapper {
        private ByteArrayOutputStream out;
        private int contentLength;
        private String contentType;

        public JsonPResponseWrapper(HttpServletResponse response) {
            super(response);
            out = new ByteArrayOutputStream();
        }

        public byte[] getData() {
            return out.toByteArray();
        }

        public ServletOutputStream getOutputStream() {
            return new ServletOutputStream() {
                private DataOutputStream dos = new DataOutputStream(out);

                public void write(int b) throws IOException {
                    dos.write(b);
                }

                public void write(byte[] b) throws IOException {
                    dos.write(b);
                }

                public void write(byte[] b, int off, int len) throws IOException {
                    dos.write(b, off, len);
                }
            };
        }

        public PrintWriter getWriter() {
            return new PrintWriter(getOutputStream(), true);
        }

        public void setContentLength(int length) {
            this.contentLength = length;
            super.setContentLength(length);
        }

        @SuppressWarnings("unused") public int getContentLength() {
            return contentLength;
        }

        public void setContentType(String type) {
            this.contentType = type;
            super.setContentType(type);
        }

        public String getContentType() {
            return contentType;
        }
    }
}
