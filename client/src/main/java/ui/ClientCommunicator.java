package ui;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import requests.RegisterRequest;
import results.CreateGameResult;
import results.ListGamesResult;
import results.UserResult;

public class ClientCommunicator {
    public ClientCommunicator() {
    }
    public ListGamesResult getCommunicator(String urlString, String authToken) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");
        connection.addRequestProperty("Authorization", authToken);
        connection.connect();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            // Get HTTP response headers, if necessary
            connection.getHeaderField("Authorization");

            InputStream responseBody = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(responseBody);
            Gson gson = new Gson();
            ListGamesResult listGamesResult =  gson.fromJson(reader, ListGamesResult.class);
            return listGamesResult;
        } else {
            // SERVER RETURNED AN HTTP ERROR

            InputStream responseBody = connection.getErrorStream();
            Gson gson = new Gson();
            ListGamesResult listGamesResult =  gson.fromJson(responseBody.toString(), ListGamesResult.class);
            return listGamesResult;
        }
    }

    public UserResult postRegisterCommunicator(String urlString, String jsonString) throws IOException, URISyntaxException {
//        URL url = new URL(urlString);
        URL url = (new URI(urlString)).toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        connection.connect();

        try(OutputStream requestBody = connection.getOutputStream();) {
            requestBody.write(jsonString.getBytes());
        }

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            // Get HTTP response headers, if necessary
             Map<String, List<String>> headers = connection.getHeaderFields();

            // OR

            String authToken = connection.getHeaderField("Authorization");

            InputStream responseBody = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(responseBody);
            Gson gson = new Gson();
            UserResult userResult =  gson.fromJson(reader, UserResult.class);
            return userResult;
        }
        else {
            // SERVER RETURNED AN HTTP ERROR

            InputStream responseBody = connection.getErrorStream();
            // Read and process error response body from InputStream ...
            return null;
        }
    }

    public UserResult postLoginCommunicator(String urlString, String jsonString) throws IOException, URISyntaxException {
        URL url = (new URI(urlString)).toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        connection.connect();

        try(OutputStream requestBody = connection.getOutputStream();) {
            requestBody.write(jsonString.getBytes());
        }

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream responseBody = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(responseBody);
            Gson gson = new Gson();
            UserResult userResult =  gson.fromJson(reader, UserResult.class);
            return userResult;
        }
        else {
            // SERVER RETURNED AN HTTP ERROR

            InputStream responseBody = connection.getErrorStream();
            // Read and process error response body from InputStream ...
            return null;
        }

    }

    public CreateGameResult postGameCommunicator(String urlString, String jsonString, String authToken) throws IOException, URISyntaxException {
        URL url = (new URI(urlString)).toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

//         Set HTTP request headers, if necessary
         connection.addRequestProperty("Authorization", authToken);

        connection.connect();

        try(OutputStream requestBody = connection.getOutputStream();) {
            requestBody.write(jsonString.getBytes());
        }

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream responseBody = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(responseBody);
            Gson gson = new Gson();
            CreateGameResult createGameResult =  gson.fromJson(reader, CreateGameResult.class);
            return createGameResult;
            // Read response body from InputStream ...
//            return "ok";
        }
        else {
            // SERVER RETURNED AN HTTP ERROR

            InputStream responseBody = connection.getErrorStream();
            // Read and process error response body from InputStream ...
            return null;
        }

    }


}
