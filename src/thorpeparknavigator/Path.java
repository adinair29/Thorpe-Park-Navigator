/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thorpeparknavigator;

/**
 *
 * @author aditya nair
 */
public class Path {

    private final int id;
    private final Ride fromRide;
    private final Ride toRide;
    private final int weights;

    public Path(int id, Ride fromRide, Ride toRide, int weights) {
        this.id = id;
        this.fromRide = fromRide;
        this.toRide = toRide;
        this.weights = weights;
    }

    public int getId() {
        return id;
    }

    public Ride getToRide() {
        return toRide;
    }

    public Ride getFromRide() {
        return fromRide;
    }

    public int getWeights() {
        return weights;
    }

}
