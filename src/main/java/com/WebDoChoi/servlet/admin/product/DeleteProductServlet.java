package com.WebDoChoi.servlet.admin.product;

import com.WebDoChoi.beans.Category;
import com.WebDoChoi.beans.Product;
import com.WebDoChoi.service.CategoryService;
import com.WebDoChoi.service.ProductService;
import com.WebDoChoi.utils.CsrfUtils;
import com.WebDoChoi.utils.ImageUtils;
import com.WebDoChoi.utils.Protector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "DeleteProductServlet", value = "/admin/productManager/delete")
public class DeleteProductServlet extends HttpServlet {
    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = Protector.of(() -> Long.parseLong(request.getParameter("id"))).get(0L);
        Optional<Product> productFromServer = Protector.of(() -> productService.getById(id)).get(Optional::empty);

        if (productFromServer.isPresent()) {
            String successMessage = String.format("Xóa sản phẩm #%s thành công!", id);
            String errorMessage = String.format("Xóa sản phẩm #%s thất bại!", id);

            Optional<Category> categoryFromServer = Protector.of(() -> categoryService.getByProductId(id)).get(Optional::empty);

            Protector.of(() -> {
                        categoryFromServer.ifPresent(category -> productService.deleteProductCategory(id, category.getId()));
                        productService.delete(id);
                        Optional.ofNullable(productFromServer.get().getImageName()).ifPresent(ImageUtils::delete);
                    })
                    .done(r -> request.getSession().setAttribute("successMessage", successMessage))
                    .fail(e -> request.getSession().setAttribute("errorMessage", errorMessage));
        }

        response.sendRedirect(request.getContextPath() + "/admin/productManager");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!CsrfUtils.isValid(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
            return;
        }
    }
}
