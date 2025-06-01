package com.WebDoChoi.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SecuritySessionListener implements HttpSessionListener {
    private static final Logger logger = LoggerFactory.getLogger("com.WebDoChoi.security");

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        logger.info("New session created - Session ID: {}", se.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        logger.info("Session destroyed - Session ID: {}", se.getSession().getId());
    }
} 