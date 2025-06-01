package com.WebDoChoi.servlet.client;

import com.WebDoChoi.beans.User;
import com.WebDoChoi.service.UserService;
import com.WebDoChoi.utils.CsrfUtils;
import com.WebDoChoi.utils.HashingUtils;
import com.WebDoChoi.utils.Protector;
import com.WebDoChoi.utils.Validator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet(name = "SignupServlet", value = "/signup")
public class SignupServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/signupView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!CsrfUtils.isValid(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
            return;
        }

        // Lưu các parameter (tên-giá trị) vào map values
        Map<String, String> values = new HashMap<>();
        values.put("username", request.getParameter("username"));
        values.put("password", request.getParameter("password"));
        values.put("fullname", request.getParameter("fullname"));
        values.put("email", request.getParameter("email"));
        values.put("phoneNumber", request.getParameter("phoneNumber"));
        values.put("gender", request.getParameter("gender"));
        values.put("address", request.getParameter("address"));
        values.put("policy", request.getParameter("policy"));

        // Kiểm tra các parameter, lưu các vi phạm (nếu có) vào map violations
        Map<String, List<String>> violations = new HashMap<>();
        Optional<User> userFromServer = Protector.of(() -> userService.getByUsername(values.get("username")))
                .get(Optional::empty);
        Optional<User> userByEmail = Protector.of(() -> userService.getByEmail(values.get("email")))
                .get(Optional::empty);
        Optional<User> userByPhoneNumber = Protector.of(() -> userService.getByPhoneNumber(values.get("phoneNumber")))
                .get(Optional::empty);

        violations.put("usernameViolations", Validator.of(values.get("username"))
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .isAtMostOfLength(25)
                .isNotExistent(userFromServer.isPresent(), "Tên đăng nhập")
                .toList());
        violations.put("passwordViolations", Validator.of(values.get("password"))
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .isAtLeastOfLength(8)
                .hasUpperCase()
                .hasLowerCase()
                .hasDigit()
                .hasSpecialChar()
                .toList());
        violations.put("fullnameViolations", Validator.of(values.get("fullname"))
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .toList());
        violations.put("emailViolations", Validator.of(values.get("email"))
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .hasPattern("^[^@]+@[^@]+\\.[^@]+$", "email")
                .isNotExistent(userByEmail.isPresent(), "Email")
                .toList());
        violations.put("phoneNumberViolations", Validator.of(values.get("phoneNumber"))
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .hasPattern("^\\d{10,11}$", "số điện thoại")
                .isNotExistent(userByPhoneNumber.isPresent(), "Số điện thoại")
                .toList());
        violations.put("genderViolations", Validator.of(values.get("gender"))
                .isNotNull()
                .toList());
        violations.put("addressViolations", Validator.of(values.get("address"))
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .toList());
        violations.put("policyViolations", Validator.of(values.get("policy"))
                .isNotNull()
                .toList());

        // Tính tổng các vi phạm sau kiểm tra (nếu có)
        int sumOfViolations = violations.values().stream().mapToInt(List::size).sum();
        String successMessage = "Đã đăng ký thành công!";
        String errorMessage = "Đã có lỗi truy vấn!";

        // Khi không có vi phạm trong kiểm tra các parameter
        if (sumOfViolations == 0) {
            try {
                User user = new User(
                        0L,
                        values.get("username"),
                        HashingUtils.hash(values.get("password")),
                        values.get("fullname"),
                        values.get("email"),
                        values.get("phoneNumber"),
                        values.get("gender").equals("1") ? 1 : 0,
                        values.get("address"),
                        "CUSTOMER"
                );
                
                userService.insert(user);
                request.setAttribute("successMessage", successMessage);
                request.getRequestDispatcher("/WEB-INF/views/signupView.jsp").forward(request, response);
                return;
            } catch (Exception e) {
                request.setAttribute("values", values);
                request.setAttribute("errorMessage", errorMessage);
            }
        } else {
            // Khi có vi phạm
            request.setAttribute("values", values);
            request.setAttribute("violations", violations);
        }

        request.getRequestDispatcher("/WEB-INF/views/signupView.jsp").forward(request, response);
    }
}
