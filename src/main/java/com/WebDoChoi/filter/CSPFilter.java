
package com.WebDoChoi.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class CSPFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResp = null;
        if (response instanceof HttpServletResponse) {
            httpResp = (HttpServletResponse) response;


            httpResp.setHeader("Content-Security-Policy",
                    "default-src 'self'; " +
                            "script-src 'self'; " +
                            "style-src 'self'; " +
                            "font-src 'self' data:; " +
                            "img-src 'self' data:; " +
                            "form-action 'self'; " +
                            "frame-ancestors 'none'; " +
                            "base-uri 'self';");
        }
        httpResp.setHeader("X-Frame-Options", "DENY");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}

