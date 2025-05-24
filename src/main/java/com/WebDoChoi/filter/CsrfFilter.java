package com.WebDoChoi.filter;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

import com.WebDoChoi.utils.CsrfUtils;

@WebFilter("/*")
public class CsrfFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();

        // Luôn tạo CSRF token nếu chưa có
        if (session.getAttribute("csrfToken") == null) {
            String token = UUID.randomUUID().toString();
            session.setAttribute("csrfToken", token);
        }

        // Nếu là POST, kiểm tra token
        if ("POST".equalsIgnoreCase(req.getMethod())) {
            String tokenInSession = (String) session.getAttribute("csrfToken");
            String tokenInRequest = req.getParameter("csrfToken");

            if (tokenInSession == null || !tokenInSession.equals(tokenInRequest)) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
