package com.WebDoChoi.servlet.admin.user;

import com.WebDoChoi.beans.User;
import com.WebDoChoi.service.UserService;
import com.WebDoChoi.utils.CsrfUtils;
import com.WebDoChoi.utils.Protector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "UserDetailServlet", value = "/admin/userManager/detail")
public class UserDetailServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = Protector.of(() -> Long.parseLong(request.getParameter("id"))).get(0L);
        Optional<User> userFromServer = Protector.of(() -> userService.getById(id)).get(Optional::empty);

        if (userFromServer.isPresent()) {
            request.setAttribute("user", userFromServer.get());
            request.getRequestDispatcher("/WEB-INF/views/userDetailView.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/userManager");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!CsrfUtils.isValid(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
            return;
        }
    }
}
