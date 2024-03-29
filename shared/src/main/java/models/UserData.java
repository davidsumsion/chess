package models;

public class UserData {
    String username;
    String password;
    String email;
    String authToken;

    public UserData(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public boolean equals(UserData obj) {
        return this.username.equals(obj.username) && this.password.equals(obj.password);
//        return super.equals(obj);
    }


    public String getEmail() {
        return email;
    }

}
