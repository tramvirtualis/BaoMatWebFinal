package com.WebDoChoi.servlet.client;

import com.WebDoChoi.beans.User;
import com.WebDoChoi.service.UserService;
import com.WebDoChoi.utils.CsrfUtils;
import com.WebDoChoi.utils.HashingUtils;
import com.WebDoChoi.utils.SecurityLogger;
import com.WebDoChoi.utils.Validator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ChangePassword", value = "/changePassword")
public class ChangePasswordServlet extends HomeServlet {
    private final UserService userService = new UserService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/views/changePasswordView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!CsrfUtils.isValid(request)) {
            SecurityLogger.logCsrfViolation(request);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
            return;
        }

        Map<String, String> values = new HashMap<>();
        values.put("currentPassword", request.getParameter("currentPassword"));
        values.put("newPassword", request.getParameter("newPassword"));
        values.put("newPasswordAgain", request.getParameter("newPasswordAgain"));

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");

        Map<String, List<String>> violations = new HashMap<>();
        violations.put("currentPasswordViolations", Validator.of(values.get("currentPassword"))
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .changeTo(HashingUtils.hash(values.get("currentPassword")))
                .isEqualTo(user.getPassword(), "Mật khẩu hiện tại")
                .toList());

        violations.put("newPasswordViolations", Validator.of(values.get("newPassword"))
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .isAtLeastOfLength(8)
                .hasUpperCase()
                .hasLowerCase()
                .hasDigit()
                .hasSpecialChar()
                .toList());

        violations.put("newPasswordAgainViolations", Validator.of(values.get("newPasswordAgain"))
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .isEqualTo(values.get("newPassword"), "Mật khẩu mới")
                .toList());

        int sumOfViolations = violations.values().stream().mapToInt(List::size).sum();

        if (sumOfViolations == 0) {
            String newPassword = HashingUtils.hash(values.get("newPassword"));
            userService.changePassword(user.getId(), newPassword);
            SecurityLogger.logPasswordChange(user.getUsername(), true, request);
            String successMessage = "Đổi mật khẩu thành công!";
            request.setAttribute("successMessage", successMessage);
        } else {
            SecurityLogger.logPasswordChange(user.getUsername(), false, request);
            request.setAttribute("violations", violations);
            String errorMessage = "Đổi mật khẩu thất bại!";
            request.setAttribute("errorMessage", errorMessage);
        }

        request.getRequestDispatcher("/WEB-INF/views/changePasswordView.jsp").forward(request, response);
    }
}
