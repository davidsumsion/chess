package requests;

public class LoginRequest {
    private String username;
    private String password;

    private String authToken;

    public LoginRequest(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


}
