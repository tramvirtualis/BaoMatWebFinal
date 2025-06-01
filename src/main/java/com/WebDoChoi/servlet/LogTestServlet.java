package com.WebDoChoi.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.WebDoChoi.utils.SecurityLogger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet(name = "LogTestServlet", value = "/logtest")
public class LogTestServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(LogTestServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");

        // Test various log levels
        logger.debug("Debug log test");
        logger.info("Info log test");
        logger.warn("Warning log test");
        
        // Test security logger
        SecurityLogger.logLoginSuccess("test_user", request);

        // Check if logs directory exists and is writable
        Path logsDir = Paths.get("logs");
        boolean logsExist = Files.exists(logsDir);
        boolean canWrite = Files.isWritable(logsDir);
        
        // Get absolute path
        String absolutePath = logsDir.toAbsolutePath().toString();

        // Write status to response
        response.getWriter().println("<html><body>");
        response.getWriter().println("<h2>Logging Test Results</h2>");
        response.getWriter().println("<p>Logs directory exists: " + logsExist + "</p>");
        response.getWriter().println("<p>Logs directory is writable: " + canWrite + "</p>");
        response.getWriter().println("<p>Logs directory absolute path: " + absolutePath + "</p>");
        response.getWriter().println("<p>Check your console and the following files:</p>");
        response.getWriter().println("<ul>");
        response.getWriter().println("<li>logs/security.log</li>");
        response.getWriter().println("<li>logs/debug.log</li>");
        response.getWriter().println("</ul>");
        response.getWriter().println("</body></html>");
    }
} 