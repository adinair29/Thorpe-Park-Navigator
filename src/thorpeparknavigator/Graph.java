/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thorpeparknavigator;

import java.util.List;

/**
 *
 * @author aditya nair
 */
public class Graph {

    private final List<Ride> rides;
    private final List<Path> weights;

    public Graph(List<Ride> rides, List<Path> weights) {
        this.rides = rides;
        this.weights = weights;
    }

    public List<Ride> getRides() {
        return rides;
    }

    public List<Path> getWeights() {
        return weights;
    }

}
