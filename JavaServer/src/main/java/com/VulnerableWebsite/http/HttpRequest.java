package com.VulnerableWebsite.http;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Hashtable;
import java.util.Set;

public class HttpRequest extends HttpMessage{

    private HttpMethod method;
    private String requestTarget;
    private String originalHttpVersion; // literal from the request
<<<<<<< HEAD
    private String queryString;
    private HttpVersion bestCompatibleHttpVersion;
=======
    //private String queryString;
    private HttpVersion bestCompatibleHttpVersion;
    private String body;
>>>>>>> master

    HttpRequest() {
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getRequestTarget() {
        return requestTarget;
    }

    public HttpVersion getBestCompatibleHttpVersion() {
        return bestCompatibleHttpVersion;
    }

    public String getOriginalHttpVersion() {
        return originalHttpVersion;
    }

<<<<<<< HEAD
    public String getQueryParam(String key) {
=======
    public String getBody() {
        return body;
    }

    void setBody(String body) {
        this.body = body;
    }

    /*public String getQueryParam(String key) {
>>>>>>> master
        if (requestTarget == null || requestTarget.isEmpty()) {
            return null;
        }
        Map<String, String> queryParams = new HashMap<>();
        for (String param : requestTarget.split("&")) {
            String[] parts = param.split("=");
            parts[0] = parts[0].replace("+", " ");
            if (parts.length == 2) {
                queryParams.put(parts[0], parts[1]);
            }
        }
        return queryParams.get(key);
<<<<<<< HEAD
    }
=======
    }*/
>>>>>>> master

    void setMethod(String methodName) throws HttpParsingException {
        for (HttpMethod method : HttpMethod.values()) {
            if (methodName.equals(method.name())) {
                this.method = method;
                return;
            }
        }
        throw new HttpParsingException(
                HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED
        );
    }

    void setRequestTarget(String requestTarget) throws HttpParsingException {
        if (requestTarget == null || requestTarget.length() == 0) {
            throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);
        }
        this.requestTarget = requestTarget;
    }

    void setHttpVersion(String originalHttpVersion) throws BadHttpVersionException, HttpParsingException {
        this.originalHttpVersion = originalHttpVersion;
        this.bestCompatibleHttpVersion = HttpVersion.getBestCompatibleVersion(originalHttpVersion);
        if (this.bestCompatibleHttpVersion == null) {
            throw new HttpParsingException(
                    HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED
            );
        }
    }


}
