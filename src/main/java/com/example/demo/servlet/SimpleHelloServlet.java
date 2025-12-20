
package com.example.demo.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class SimpleHelloServlet extends HttpServlet {

    private String message = "Hello from servlet";

    @Override
    public void init(ServletConfig config) throws ServletException {
        if (config != null) super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("text/plain");
        resp.getWriter().write(message);
    }
}
