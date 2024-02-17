package server.requests;

public class RegisterRequest {
    private String userName;
    private String password;
    private String email;

    public RegisterRequest(String userName, String password, String email){
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
