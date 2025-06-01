package com.WebDoChoi.utils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CsrfUtils {

    public static boolean isValid(HttpServletRequest request) {
        String token = request.getParameter("csrfToken");
        HttpSession session = request.getSession(false);
        String sessionToken = (session != null) ? (String) session.getAttribute("csrfToken") : null;
        return token != null && token.equals(sessionToken);
       // return true;
    }
}
