package com.example.demo.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/hello")
public class SimpleHelloServlet extends HttpServlet {
    private String message = "Hello from servlet";

    @Override
    public void init(ServletConfig config) throws ServletException {
        // Requirement: Handle both non-null and null config gracefully
        if (config != null) {
            super.init(config);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.getWriter().write(message);
    }
}