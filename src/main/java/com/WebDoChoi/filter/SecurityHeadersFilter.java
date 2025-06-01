package com.WebDoChoi.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.annotation.WebFilter;

@WebFilter("/*") // Lọc tất cả các request
public class SecurityHeadersFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Không cần khởi tạo gì thêm
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {


        if (response instanceof HttpServletResponse) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;

            // Thêm các header bảo mật
            if (httpServletResponse.getHeader("X-Content-Type-Options") == null) {
                httpServletResponse.setHeader("X-Content-Type-Options", "nosniff");
            }

            /*
            if (httpServletResponse.getHeader("X-Frame-Options") == null) {
                httpServletResponse.setHeader("X-Frame-Options", "SAMEORIGIN");
            }*/
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Không cần dọn dẹp gì
    }
}
