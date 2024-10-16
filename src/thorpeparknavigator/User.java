/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thorpeparknavigator;

/**
 *
 * @author aditya nair
 */
public class User {

    final private String userId;
    final private String userName;
    final private String password;
    final private String currentLocation;
    final private Integer maxWaitingTime;

    public User(String userId, String userName, String password,
            String currentLocation, Integer maxWaitingTime) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.currentLocation = currentLocation;
        this.maxWaitingTime = maxWaitingTime;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public Integer getMaxWaitingTime() {
        return maxWaitingTime;
    }

}
