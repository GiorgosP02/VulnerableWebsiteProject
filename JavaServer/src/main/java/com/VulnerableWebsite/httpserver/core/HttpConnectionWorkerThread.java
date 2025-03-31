package com.VulnerableWebsite.httpserver.core;

import com.VulnerableWebsite.http.*;
import com.VulnerableWebsite.httpserver.core.io.ReadFileException;
import com.VulnerableWebsite.httpserver.core.io.WebRootHandler;
import com.VulnerableWebsite.httpserver.core.io.NintendoGamesHandler;
import com.VulnerableWebsite.httpserver.core.io.LoginHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.net.Socket;

public class HttpConnectionWorkerThread extends Thread {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);
    private Socket socket;
    private WebRootHandler webRootHandler;
    private HttpParser httpParser = new HttpParser();

    public HttpConnectionWorkerThread(Socket socket, WebRootHandler webRootHandler) {
        this.socket = socket;
        this.webRootHandler = webRootHandler;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            HttpRequest request = httpParser.parseHttpRequest(inputStream);
            HttpResponse response = handleRequest(request);

            outputStream.write(response.getResponseBytes());

            LOGGER.info(" * Connection Processing Finished.");
        } catch (IOException e) {
            LOGGER.error("Problem with communication", e);
        } catch (HttpParsingException e) {
            LOGGER.info("Bag Request", e);

            HttpResponse response = new HttpResponse.Builder()
                    .httpVersion(HttpVersion.HTTP_1_1.LITERAL)
                    .statusCode(e.getErrorCode())
                    .build();
            try {
                outputStream.write(response.getResponseBytes());
            } catch (IOException ex) {
                LOGGER.error("Problem with communication", e);
            }

        } finally {
            if (inputStream!= null) {
                try {
                    inputStream.close();
                } catch (IOException e) {}
            }
            if (outputStream!=null) {
                try {
                    outputStream.close();
                } catch (IOException e) {}
            }
            if (socket!= null) {
                try {
                    socket.close();
                } catch (IOException e) {}
            }
        }
    }

    private HttpResponse handleRequest(HttpRequest request) {
        switch (request.getMethod()) {
            case GET:
                LOGGER.info(" * GET Request");
                return handleGetRequest(request, true);
            case HEAD:
                LOGGER.info(" * HEAD Request");
                return handleGetRequest(request, false);
            case POST:
                LOGGER.info(" * POST Request");
                return handlePostRequest(request);
            default:
                return new HttpResponse.Builder()
                        .httpVersion(request.getBestCompatibleHttpVersion().LITERAL)
                        .statusCode(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED)
                        .build();
        }
    }


    private HttpResponse handleGetRequest(HttpRequest request, boolean setMessageBody) {
        try {
            if (request.getRequestTarget().startsWith("/NintendoGames.html")) {
                String searchQuery = null;
                if (request.getRequestTarget().contains("?search=")) {
                    searchQuery = request.getRequestTarget().split("\\?search=")[1];
                    searchQuery = searchQuery.replace("+", " ").trim();
                }

                // Generate HTML with optional filtering
                String htmlContent = NintendoGamesHandler.generateHtml(searchQuery);
                byte[] messageBody = htmlContent.getBytes(StandardCharsets.UTF_8);

                return new HttpResponse.Builder()
                        .httpVersion(request.getBestCompatibleHttpVersion().LITERAL)
                        .statusCode(HttpStatusCode.OK)
                        .addHeader(HttpHeaderName.CONTENT_TYPE.headerName, "text/html")
                        .addHeader(HttpHeaderName.CONTENT_LENGTH.headerName, String.valueOf(messageBody.length))
                        .messageBody(messageBody)
                        .build();
            }

            if (request.getRequestTarget().startsWith("/login?")) {
                String loginQuery = null;
                loginQuery = request.getRequestTarget().split("/login\\?")[1];
                String username = loginQuery.split("&")[0].split("=")[1].replace("+", " ");
                String password = loginQuery.split("&")[1].split("=")[1];

                String authenticatedUser = UserAuthenticator.authenticateUser(username, password);

                HttpResponse.Builder responseBuilder = new HttpResponse.Builder()
                        .statusCode(authenticatedUser != null ? HttpStatusCode.OK : HttpStatusCode.CLIENT_ERROR_403_FORBIDDEN);

                if (authenticatedUser != null) {
                    String sessionToken = authenticatedUser + "123"; // Insecure session token
                    responseBuilder.addHeader("Set-Cookie", "session=" + sessionToken + "; Path=/");
                }

                String htmlContent = LoginHandler.generateHtml(authenticatedUser);
                byte[] messageBody = htmlContent.getBytes(StandardCharsets.UTF_8);

                return responseBuilder
                        .httpVersion(request.getBestCompatibleHttpVersion().LITERAL)
                        .addHeader(HttpHeaderName.CONTENT_TYPE.headerName, "text/html")
                        .addHeader(HttpHeaderName.CONTENT_LENGTH.headerName, String.valueOf(messageBody.length))
                        .messageBody(messageBody)
                        .build();
            }


            // Default file serving
            HttpResponse.Builder builder = new HttpResponse.Builder()
                    .httpVersion(request.getBestCompatibleHttpVersion().LITERAL)
                    .statusCode(HttpStatusCode.OK)
                    .addHeader(HttpHeaderName.CONTENT_TYPE.headerName, webRootHandler.getFileMimeType(request.getRequestTarget()));

            if (setMessageBody) {
                byte[] messageBody = webRootHandler.getFileByteArrayData(request.getRequestTarget());
                builder.addHeader(HttpHeaderName.CONTENT_LENGTH.headerName, String.valueOf(messageBody.length))
                        .messageBody(messageBody);
            }

            return builder.build();
        } catch (FileNotFoundException e) {
            return new HttpResponse.Builder()
                    .httpVersion(request.getBestCompatibleHttpVersion().LITERAL)
                    .statusCode(HttpStatusCode.CLIENT_ERROR_404_NOT_FOUND)
                    .build();
        } catch (ReadFileException e) {
            return new HttpResponse.Builder()
                    .httpVersion(request.getBestCompatibleHttpVersion().LITERAL)
                    .statusCode(HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR)
                    .build();
        }
    }


    private HttpResponse handlePostRequest(HttpRequest request) {
        if ("/addGame".equals(request.getRequestTarget())) {
            try {
                String requestBody = new String(request.getMessageBody(), StandardCharsets.UTF_8);
                String gameTitle = requestBody.split("=")[1].replace("+", " ").trim(); // Extract title

                if (!gameTitle.isEmpty()) {
                    GameRepository.addGame(gameTitle); // Insert into database
                }

                // Redirect back to the game list page
                return new HttpResponse.Builder()
                        .httpVersion(request.getBestCompatibleHttpVersion().LITERAL)
                        .statusCode(HttpStatusCode.REDIRECTION_303_SEE_OTHER)
                        .addHeader("Location", "/NintendoGames.html")
                        .build();
            } catch (Exception e) {
                return new HttpResponse.Builder()
                        .httpVersion(request.getBestCompatibleHttpVersion().LITERAL)
                        .statusCode(HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR)
                        .build();
            }
        }

        return new HttpResponse.Builder()
                .httpVersion(request.getBestCompatibleHttpVersion().LITERAL)
                .statusCode(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST)
                .build();
    }
}
