package com.WebDoChoi.servlet.client;

import com.WebDoChoi.beans.User;
import com.WebDoChoi.service.CartService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "UserServlet", value = "/user")
public class UserServlet extends HttpServlet {

    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // ❗Không tự tạo session mới
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login"); // 🚫 Không có session → redirect
            return;
        }

        User user = (User) session.getAttribute("currentUser");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login"); // 🚫 Không có user trong session → redirect
            return;
        }

        // ✅ Gọi logic xử lý
        int countCartItemQuantityByUserId = cartService.countCartItemQuantityByUserId(user.getId());
        request.setAttribute("countCartItemQuantity", countCartItemQuantityByUserId);

        int countOrderByUserId = cartService.countOrderByUserId(user.getId());
        request.setAttribute("countOrder", countOrderByUserId);

        int countOrderDeliverByUserId = cartService.countOrderDeliverByUserId(user.getId());
        request.setAttribute("countOrderDeliver", countOrderDeliverByUserId);

        int countOrderReceivedByUserId = cartService.countOrderReceivedByUserId(user.getId());
        request.setAttribute("countOrderReceived", countOrderReceivedByUserId);

        // ✅ Chuyển tới trang view
        request.getRequestDispatcher("/WEB-INF/views/userView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED); // 🚫 Từ chối POST
    }
}
