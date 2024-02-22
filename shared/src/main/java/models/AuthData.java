package models;

public class AuthData {
    private String Username;
    private String AuthToken;
    public AuthData(String username, String authToken) {
        AuthToken = authToken;
        Username = username;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getAuthToken() {
        return AuthToken;
    }

    public void setAuthToken(String authtoken) {
        AuthToken = authtoken;
    }
}
