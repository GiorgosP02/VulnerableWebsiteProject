package com.VulnerableWebsite.httpserver.core.io;

public class LoginHandler {

    public static String generateHtml(String authenticatedUser) {

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>")
                .append("<html lang=\"en\">")
                .append("<head>")
                .append("<meta charset=\"UTF-8\">")
                .append("<title>Login Page</title>")
                .append("</head>")
                .append("<body>");

        if (authenticatedUser != null) {
            html.append("<h2>Welcome, " + authenticatedUser + "!</h2>");
        } else {
            html.append("<h2>Log in failed! Please try again.</h2>");
        }

        html
                .append("</body>")
                .append("</html>");

        return html.toString();
    }
}

