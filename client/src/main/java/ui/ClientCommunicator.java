package ui;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import results.ListGamesResult;

public class ClientCommunicator {
    public ClientCommunicator() {
    }
    public String getComunicator(String urlString, String authToken) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");
        // Set HTTP request headers, if necessary
        // connection.addRequestProperty("Accept", "text/html");
        connection.addRequestProperty("Authorization", authToken);
        connection.connect();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            // Get HTTP response headers, if necessary
            // Map<String, List<String>> headers = connection.getHeaderFields();

            // OR

            //connection.getHeaderField("Content-Length");

            InputStream responseBody = connection.getInputStream();
            Gson gson = new Gson();
            ListGamesResult listGamesResult =  gson.fromJson(responseBody.toString(), ListGamesResult.class);
            return listGamesResult.toString();

            // Read and process response body from InputStream ...
        } else {
            // SERVER RETURNED AN HTTP ERROR

            InputStream responseBody = connection.getErrorStream();
            // Read and process error response body from InputStream ...
            return "";
        }
    }

//    public void doPost(String urlString) throws IOException {
//        URL url = new URL(urlString);
//
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//        connection.setReadTimeout(5000);
//        connection.setRequestMethod("POST");
//        connection.setDoOutput(true);
//
//        // Set HTTP request headers, if necessary
//        // connection.addRequestProperty("Accept", "text/html");
//
//        connection.connect();
//
//        try(OutputStream requestBody = connection.getOutputStream();) {
//            // Write request body to OutputStream ...
//        }
//
//        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//            // Get HTTP response headers, if necessary
//            // Map<String, List<String>> headers = connection.getHeaderFields();
//
//            // OR
//
//            //connection.getHeaderField("Content-Length");
//
//            InputStream responseBody = connection.getInputStream();
//            // Read response body from InputStream ...
//        }
//        else {
//            // SERVER RETURNED AN HTTP ERROR
//
//            InputStream responseBody = connection.getErrorStream();
//            // Read and process error response body from InputStream ...
//        }
//    }
}
