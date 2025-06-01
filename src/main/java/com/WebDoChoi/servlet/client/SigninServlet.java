package com.WebDoChoi.servlet.client;

import com.WebDoChoi.beans.User;
import com.WebDoChoi.service.UserService;
import com.WebDoChoi.utils.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "SigninServlet", value = "/signin")
public class SigninServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/signinView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!CsrfUtils.isValid(request)) {
            SecurityLogger.logCsrfViolation(request);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
            return;
        }

        String username = request.getParameter("username");
        Map<String, String> values = new HashMap<>();
        values.put("username", username);
        values.put("password", request.getParameter("password"));

        Map<String, List<String>> violations = new HashMap<>();
        Optional<User> userFromServer = Protector.of(() -> userService.getByUsername(values.get("username")))
                .get(Optional::empty);

        // Check for account lockout if the username exists
        if (userFromServer.isPresent() && LoginAttemptManager.isLocked(username)) {
            long remainingMinutes = LoginAttemptManager.getRemainingLockoutTime(username) / (60 * 1000);
            request.setAttribute("errorMessage", String.format("Tài khoản đã bị khóa. Vui lòng thử lại sau %d phút.", remainingMinutes));
            SecurityLogger.logLoginFailure(username + " (account locked)", request);
            request.getRequestDispatcher("/WEB-INF/views/signinView.jsp").forward(request, response);
            return;
        }

        violations.put("usernameViolations", Validator.of(values.get("username"))
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .isAtMostOfLength(25)
                .isExistent(userFromServer.isPresent(), "Tên đăng nhập")
                .toList());
        violations.put("passwordViolations", Validator.of(values.get("password"))
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .isAtMostOfLength(32)
                .changeTo(HashingUtils.hash(values.get("password")))
                .isEqualTo(userFromServer.map(User::getPassword).orElse(""), "Mật khẩu")
                .toList());

        int sumOfViolations = violations.values().stream().mapToInt(List::size).sum();

        if (sumOfViolations == 0 && userFromServer.isPresent()) {
            // Reset login attempts on successful login
            LoginAttemptManager.resetAttempts(username);
            
            request.getSession().setAttribute("currentUser", userFromServer.get());
            User user = userFromServer.get();
            SecurityLogger.logLoginSuccess(user.getUsername(), request);
            if (Arrays.asList("ADMIN", "EMPLOYEE").contains(user.getRole())) {
                request.getSession().setAttribute("currentUser", user);
                response.sendRedirect(request.getContextPath() + "/admin");
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }
        } else {
            // Only record failed attempt if username exists
            if (userFromServer.isPresent()) {
                LoginAttemptManager.recordFailedAttempt(username);
                
                // Add remaining attempts to the error message
                int remainingAttempts = LoginAttemptManager.getRemainingAttempts(username);
                if (remainingAttempts > 0) {
                    request.setAttribute("errorMessage", String.format("Đăng nhập thất bại. Còn %d lần thử.", remainingAttempts));
                }
            }
            
            SecurityLogger.logLoginFailure(values.get("username"), request);
            request.setAttribute("values", values);
            request.setAttribute("violations", violations);
            request.getRequestDispatcher("/WEB-INF/views/signinView.jsp").forward(request, response);
        }
    }
}
