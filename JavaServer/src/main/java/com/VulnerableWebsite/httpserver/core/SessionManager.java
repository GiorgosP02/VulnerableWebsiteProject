package com.VulnerableWebsite.httpserver.core;

import com.VulnerableWebsite.http.HttpRequest;

public class SessionManager {
    public static String getAuthenticatedUser(HttpRequest request) {
        String cookieHeader = request.getHeader("Cookie");
        if (cookieHeader != null && cookieHeader.startsWith("session=")) {
            return cookieHeader.substring(8).replace("123", ""); // Insecure session parsing
        }
        return null;
    }
}
