package org.vsr.onenationoneration.others;

/**
 * Created by tanuj on 21/1/19.
 */

public class UserProfile {
    private String name,email,phone,username,sessionId;
    private static UserProfile userProfile;

    private UserProfile(){
    }

    public static UserProfile getUser(){
        if(userProfile==null){
            userProfile = new UserProfile();
        }
        return userProfile;
    }

    public static void deleteUser(){
        if(userProfile!=null)
            userProfile = null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSessionId(){
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
