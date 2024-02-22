package models;

public class AuthData {
    private String Username;
    private String Authtoken;
    public AuthData(String username, String authtoken) {
        Authtoken = authtoken;
        Username = username;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getAuthtoken() {
        return Authtoken;
    }

    public void setAuthtoken(String authtoken) {
        Authtoken = authtoken;
    }
}
