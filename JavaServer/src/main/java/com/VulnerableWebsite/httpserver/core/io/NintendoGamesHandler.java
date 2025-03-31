package com.VulnerableWebsite.httpserver.core.io;

import com.VulnerableWebsite.httpserver.core.Game;
import com.VulnerableWebsite.httpserver.core.GameRepository;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class NintendoGamesHandler {

    public static String generateHtml(String searchQuery) {
        List<Game> games;
        if (searchQuery != null && !searchQuery.isEmpty()) {
            games = GameRepository.searchGames(searchQuery);
        } else {
            games = GameRepository.getAllGames();
        }

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>")
                .append("<html lang=\"en\">")
                .append("<head>")
                .append("<meta charset=\"UTF-8\">")
                .append("<title>Nintendo Games</title>")
                .append("</head>")
                .append("<body>")
                .append("<h1>Games in stock!</h1>")
                .append("<form action=\"/NintendoGames.html\" method=\"get\">")
                .append("<input type=\"text\" name=\"search\" placeholder=\"Search games...\" value=\"" + (searchQuery != null ? searchQuery : "") + "\">")
                .append("<button type=\"submit\">Search</button>")
                .append("</form>")
                .append("<form action=\"/NintendoGames.html\" method=\"post\">")
                .append("<input type=\"text\" name=\"title\" placeholder=\"Enter game title...\" required>")
                .append("<button type=\"submit\">Add Game</button>")
                .append("</form>")
                .append("<table border='1'>")
                .append("<thead><tr><th>Title</th></tr></thead>")
                .append("<tbody>");

       // Vulnerability ID 2 - Reflected XSS
        if (searchQuery != null && !searchQuery.isEmpty()) {
            html.append("<h2>Results for: ").append(searchQuery
                    .replace("%3C", "<")
                    .replace("%3E", ">")).append("</h2>");
        }

        for (Game game : games) {
            html.append("<tr>")
                    .append("<td>").append(game.getTitle()).append("</td>")
                    .append("</tr>");
        }

        html.append("</tbody>")
                .append("</table>")
                .append("</body>")
                .append("</html>");

        return html.toString();
    }
}
