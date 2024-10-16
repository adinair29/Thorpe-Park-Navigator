/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thorpeparknavigator;

/**
 *
 * @author aditya nair
 */
public class Preferences {

    private final int id;
    private final User user;
    private final Ride ride;
    private final int priorityOrder;
    private final int rideQueueTime;

    public Preferences(int id, User user, Ride ride, int priorityOrder, int rideQueueTime) {
        this.id = id;
        this.user = user;
        this.ride = ride;
        this.priorityOrder = priorityOrder;
        this.rideQueueTime = rideQueueTime;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Ride getRide() {
        return ride;
    }

    public int getPriorityOrder() {
        return priorityOrder;
    }

    public int getRideQueueTime() {
        return rideQueueTime;
    }

}
