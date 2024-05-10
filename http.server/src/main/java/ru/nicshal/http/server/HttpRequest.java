package ru.nicshal.http.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nicshal.http.server.utils.HttpMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequest {

    private final Logger logger = LogManager.getLogger(HttpRequest.class.getName());
    private final String rawRequest;
    private String uri;
    private HttpMethod method;
    private Map<String, String> parameters;
    private String body;

    public HttpRequest(String rawRequest) {
        this.rawRequest = rawRequest;
        this.parseRequestLine();
    }

    public String getUri() {
        return uri;
    }

    public String getRouteKey() {
        return String.format("%s %s", method, uri);
    }

    public String getParameter(String key) {
        return parameters.getOrDefault(key, null);
    }

    public String getBody() {
        return body;
    }

    public void info(boolean showRawRequest) {
        if (showRawRequest) {
            logger.info(rawRequest);
        }
        logger.info("URI: " + uri);
        logger.info("HTTP-method: " + method);
        logger.info("Parameters: " + parameters);
        logger.info("Body: " + body);
    }

    private void parseRequestLine() {
        int startIndex = rawRequest.indexOf(' ');
        int endIndex = rawRequest.indexOf(' ', startIndex + 1);
        this.uri = rawRequest.substring(startIndex + 1, endIndex);
        this.method = HttpMethod.valueOf(rawRequest.substring(0, startIndex));
        this.parameters = new HashMap<>();
        if (uri.contains("?")) {
            String[] elements = uri.split("[?]");
            this.uri = elements[0];
            String[] keysValues = elements[1].split("&");
            for (String o : keysValues) {
                String[] keyValue = o.split("=");
                this.parameters.put(keyValue[0], keyValue[1]);
            }
        }
        if (this.method != HttpMethod.GET) {
            this.tryToParseBody();
        }
    }

    private void tryToParseBody() {
        List<String> lines = rawRequest.lines().collect(Collectors.toList());
        int splitLine = -1;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).isEmpty()) {
                splitLine = i;
                break;
            }
        }
        if (splitLine > -1) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = splitLine + 1; i < lines.size(); i++) {
                stringBuilder.append(lines.get(i));
            }
            this.body = stringBuilder.toString();
        }
    }

}