import com.google.gson.*;
import com.sun.net.httpserver.HttpServer;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Formatter implements Server {

    @NotNull
    private final HttpServer server;
    @NotNull
    private final Gson builder;

    private static final int PORT = 8000;
    private static final int CODE_OK = 200;
    private static final String ROOT = "/";

  
    public Formatter() throws IOException {
        this.builder = new GsonBuilder().setPrettyPrinting().create();
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        Pattern pattern = Pattern.compile("[^:]+:(.*) at line (\\d+) column (\\d+) .*");
        AtomicInteger requestIds = new AtomicInteger(0);
        this.server.createContext(ROOT, http -> {
            InputStreamReader isr = new InputStreamReader(http.getRequestBody());
            final String jsonRequest = new BufferedReader(isr).lines().collect(Collectors.joining());
            System.out.println("request:" + jsonRequest);
            String jsonResponse;
            try {
                Object object = builder.fromJson(jsonRequest, Object.class);
                jsonResponse = builder.toJson(object);
            } catch (JsonSyntaxException e) {
                Matcher matcher = pattern.matcher(e.getMessage());
                String message = matcher.group(0);
                String line = matcher.group(1);
                String column = matcher.group(2);
                JsonObject jsonError = new JsonObject();
                String resourceName = http.getRequestMethod().equals("PUT") ? http.getRequestURI().getPath() : "json string";
                jsonError.addProperty("errorCode", message.hashCode());
                jsonError.addProperty("errorMessage", message);
                jsonError.addProperty("errorPlace", "line " + line + " column " + column);
                jsonError.addProperty("resource", resourceName);
                jsonError.addProperty("request-id", requestIds.getAndIncrement());
                jsonResponse = builder.toJson(jsonError);
            }
            System.out.println("response:" + jsonResponse);
            http.sendResponseHeaders(CODE_OK, jsonResponse.length());
            http.getResponseBody().write(jsonResponse.getBytes());
            http.close();
        });
    }


    public static void main(String[] args) throws IOException {
        Formatter formatter = new Formatter();
        formatter.start();
        Runtime.getRuntime().addShutdownHook(new Thread(formatter::stop));
    }

   
    @Override
    public void start() {
        this.server.start();
    }

    @Override
    public void stop() {
        this.server.stop(0);
    }
}