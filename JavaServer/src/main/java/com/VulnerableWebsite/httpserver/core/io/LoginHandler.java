package com.VulnerableWebsite.httpserver.core.io;

public class LoginHandler {

<<<<<<< HEAD
    public static String generateHtml(String authenticatedUser) {
=======
    public static String generateHtml(String authenticatedUser, boolean is_admin) {
>>>>>>> master

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
<<<<<<< HEAD
=======
            if (is_admin) {
                html.append("<br>");
                html.append("<a href=\"/admin.html\">Admin Dashboard</a>\n");
            }
>>>>>>> master
        } else {
            html.append("<h2>Log in failed! Please try again.</h2>");
        }

        html
                .append("</body>")
                .append("</html>");

        return html.toString();
    }
}

