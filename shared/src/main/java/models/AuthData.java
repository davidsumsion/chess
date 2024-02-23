package models;

public class AuthData {
    private String username;
    private String authToken;
    public AuthData(String username, String authToken) {
        this.authToken = authToken;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authtoken) {
        authToken = authtoken;
    }
}
