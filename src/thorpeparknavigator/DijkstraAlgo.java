/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thorpeparknavigator;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Scanner;

/**
 *
 * @author aditya nair
 */
public class DijkstraAlgo {

    private final List<Ride> rides;
    private final List<Path> paths;
    private Set<Ride> visitedNodes;
    private Set<Ride> unvisitedNodes;
    private Map<Ride, Ride> predecessors;
    private Map<Ride, Integer> distance;
    private Map<Ride, Integer> eachStep;

    public DijkstraAlgo(Graph graph) {
        // create a copy of the array so that we can operate on this array
        this.rides = new ArrayList<Ride>(graph.getRides());
        this.paths = new ArrayList<Path>(graph.getWeights());
    }

    public static void main(String arg[]) {

        boolean exit = false;
        while (!exit) {

            Scanner scanner = new Scanner(System.in);

            List<Ride> tnodes;
            List<Path> tedges;
            List<User> users;
            List<Preferences> preferences;

            tnodes = new ArrayList<Ride>();
            tedges = new ArrayList<Path>();
            users = new ArrayList<User>();
            preferences = new ArrayList<Preferences>();

            DatabaseAccess dbAccess = new DatabaseAccess();
            Connection conn = dbAccess.databaseConnect();
            dbAccess.createTable(conn);
            if (conn == null) {
                System.out.println(" DB connection is null. Cannot proceed.");
            }

            tnodes = dbAccess.getRidesFromDB(conn);
            tedges = dbAccess.getEdgesFromDB(conn, tnodes);
            users = dbAccess.getUsersFromDB(conn);

            Graph graph = new Graph(tnodes, tedges);

            DijkstraAlgo dijkstra = new DijkstraAlgo(graph);

            String userName = "", password = "";
            boolean valid = false;

            User user = null;
            String userId = null;

            System.out.println("\n\n                Welcome to Thorpe Park Navigator");
            System.out.println("\nLogin / Account Registration\n");

            while (true) {

                while (true) {
                    System.out.println("Enter your existing / desired username that is between 5 and 12 characters long:");
                    userName = scanner.nextLine();
                    if (!userName.isEmpty() && userName.length() >= 5 && userName.length() <= 12) {
                        valid = true;
                        break;
                    }
                    System.out.println("The username entered is invalid. Username cannot be empty, and must be between 5 and 12 characters long.");
                }
                while (true) {
                    System.out.println("Enter your existing / desired password:");
                    password = scanner.nextLine();
                    if (!password.isEmpty()) {
                        user = dijkstra.validateUserAndPassword(userName, password, users);
                        break;
                    }
                    System.out.println("Password cannot be empty.");
                }

                if (user != null) {
                    userId = user.getUserId();
                    break;
                } else if (dijkstra.getUser(userName, users) == null && valid) {
                    System.out.print("\nThis username and password combination does not exist. Would you like to create a new account?\nEnter 'Y' to proceed\nEnter any key to go back to the start\n");
                    String createUser = scanner.nextLine();
                    createUser = createUser.toUpperCase();
                    if (createUser.equals("Y")) {
                        dbAccess.createUser(conn, userName, password);
                        valid = false;
                        users = dbAccess.getUsersFromDB(conn);
                        user = dijkstra.validateUserAndPassword(userName, password, users);
                        userId = user.getUserId();
                        System.out.println("Username: '" + userName + "' and Password: '" + password + "' are valid. Account creation successful!");
                        break;
                    }
                } else {
                    System.out.println("Username and password do not match.\n");
                }

            }

            System.out.println("\n  Ride Name                      Ride Key"); // Map Key
            System.out.println("Entrance                             A");
            System.out.println("Depth Charge                         B");
            System.out.println("Vortex                               C");
            System.out.println("Quantum                              D");
            System.out.println("Flying fish                          E");
            System.out.println("Zodiac                               F");
            System.out.println("Rush                                 G");
            System.out.println("Colossus                             H");
            System.out.println("The Walking Dead: The Ride           I");
            System.out.println("Ghost Train                          J");
            System.out.println("Samurai                              K");
            System.out.println("SAW - The Ride                       L");
            System.out.println("Storm Surge                          M");
            System.out.println("THE SWARM                            N");
            System.out.println("Tidal Wave                           O");
            System.out.println("Dodgems                              P");
            System.out.println("Detonator                            Q");
            System.out.println("Stealth                              R");
            System.out.println("Storm in a Tea Cup                   S");
            System.out.println("Nemesis Inferno                      T");
            System.out.println("Rumba Rapids                         U\n");

            HashMap<String, Integer> preferencesTable = new HashMap<String, Integer>();

            HashMap<String, Integer> queueTimesTable = new HashMap<String, Integer>();

            valid = false;
            String pref1;
            String pref2;
            String pref3;
            String userCurrentLocation = "";
            Integer maxWaitingTime = null;
            Integer qTime1 = 0;
            Integer qTime2 = 0;
            Integer qTime3 = 0;

            while (!valid || preferencesTable.size() < 3) {

                Ride ride = null;
                while (ride == null) {
                    System.out.print("Using the key above, enter where in the park you currently are:\n");
                    userCurrentLocation = scanner.nextLine();
                    if (isValidLocation(userCurrentLocation) == true) {
                        userCurrentLocation = userCurrentLocation.toUpperCase();

                        ride = dijkstra.getRide(userCurrentLocation);
                        if (ride == null) {
                            System.out.println("The ride key you have entered does not exist.");
                        }
                    }
                }

                ride = null;

                while (ride == null) {
                    boolean formatException = true;
                    System.out.println("Using the key above, enter which ride/attraction you would like to visit first:");
                    pref1 = scanner.nextLine();
                    if (isValidLocation(pref1) == true) {
                        pref1 = pref1.toUpperCase();
                        ride = dijkstra.getRide(pref1);
                        if (pref1.equals(userCurrentLocation)) {
                            System.out.println("The ride you have chosen to visit first is the same as your current location.");
                            ride = null;
                            formatException = false;
                        } else if (ride == null) {
                            System.out.println("The ride key you have entered does not exist.");
                            formatException = false;
                        } else {
                            preferencesTable.put(pref1, 1);
                            formatException = true;
                        }
                    } else {
                        formatException = false;
                    }

                    while (formatException) {
                        Scanner in = new Scanner(System.in);
                        System.out.println("Enter the current queue time for " + dijkstra.getRide(pref1).getName() + " (" + pref1 + ")");
                        try {
                            qTime1 = in.nextInt();
                            formatException = false;
                            queueTimesTable.put(pref1, qTime1);
                        } catch (InputMismatchException ime) {
                            System.out.println("Invalid input. Please enter the queue time using numbers");
                            formatException = true;
                        }
                    }

                }

                ride = null;
                while (ride == null) {
                    boolean formatException = true;
                    System.out.println("Using the key above, enter which ride/attraction you would like to visit second:");
                    pref2 = scanner.nextLine();
                    if (isValidLocation(pref2) == true) {
                        pref2 = pref2.toUpperCase();
                        ride = dijkstra.getRide(pref2);

                        if (preferencesTable.get(pref2) != null) {
                            System.out.println("The ride you have chosen to visit second is the same as the ride you would like to visit first.");
                            ride = null;
                            formatException = false;
                        } else if (ride == null) {
                            System.out.println("The ride key you have entered does not exist.");
                            formatException = false;
                        } else {
                            preferencesTable.put(pref2, 2);
                            formatException = true;
                        }
                    } else {
                        formatException = false;
                    }

                    while (formatException) {
                        Scanner in = new Scanner(System.in);
                        System.out.println("Enter the current queue time for " + dijkstra.getRide(pref2).getName() + " (" + pref2 + ")");
                        try {
                            qTime2 = in.nextInt();
                            formatException = false;
                            queueTimesTable.put(pref2, qTime2);
                        } catch (InputMismatchException ime) {
                            System.out.println("Invalid input. Please enter the queue time using numbers");
                            formatException = true;
                        }
                    }
                }

                ride = null;
                while (ride == null) {
                    boolean formatException = true;
                    System.out.println("Using the key above, enter which ride/attraction you would like to visit third:");
                    pref3 = scanner.nextLine();
                    if (isValidLocation(pref3) == true) {
                        pref3 = pref3.toUpperCase();
                        ride = dijkstra.getRide(pref3);
                        if (ride == null) {
                            System.out.println("The ride key you have entered does not exist.");
                        }
                        if (preferencesTable.get(pref3) != null) {
                            System.out.println("Invalid input. The ride you have chosen to visit third has already been selected as another one of your choices.");
                            ride = null;
                            formatException = false;
                        } else if (ride == null) {
                            System.out.println("The ride key you have entered does not exist.");
                            formatException = false;
                        } else {
                            preferencesTable.put(pref3, 3);
                            formatException = true;
                        }

                    } else {
                        formatException = false;
                    }

                    while (formatException) {
                        Scanner in = new Scanner(System.in);
                        System.out.println("Enter the current queue time for " + dijkstra.getRide(pref3).getName() + " (" + pref3 + ")");
                        try {
                            qTime3 = in.nextInt();
                            formatException = false;
                            queueTimesTable.put(pref3, qTime3);
                        } catch (InputMismatchException ime) {
                            System.out.println("Invalid input. Please enter the queue time using numbers");
                            formatException = true;
                        }
                    }
                }

                while (maxWaitingTime == null) {
                    scanner = new Scanner(System.in);
                    System.out.println("What is the maximum amount of time, in minutes, you will be willing to wait in a queue? Enter a number:");
                    try {
                        maxWaitingTime = scanner.nextInt();
                    } catch (InputMismatchException ime) {
                        System.out.println("Invalid input. Please enter your maximum waiting time in numbers");
                        maxWaitingTime = null;

                    }

                }
                valid = true;

            }

            dbAccess.savePreferences(conn, userId, userCurrentLocation, maxWaitingTime,
                    preferencesTable); //Save to DB

            dbAccess.saveQueueTime(conn, queueTimesTable); //Save to DB

            users = dbAccess.getUsersFromDB(conn);

            user = dijkstra.validateUserAndPassword(userName, password, users);

            preferences = dbAccess.getPreferenceForUserFromDB(conn, tnodes, user, userId); //Read back from DB

            if (conn != null) {
                dbAccess.close(conn);
                System.out.println("\nDatabase closed.\n");
            }

            userCurrentLocation = user.getCurrentLocation();
            Ride fromRide = dijkstra.getRide(userCurrentLocation);

            int totalCost = 0;
            int totalQueueTime = 0;
            for (Preferences preference : preferences) {
                Ride toRide = preference.getRide();
                System.out.println("");

                System.out.println("Cost of journey from '"
                        + fromRide.getName() + "' (" + fromRide.getId() + ") To '" + toRide.getName()
                        + "' (" + toRide.getId() + ") is "
                        + dijkstra.getShortestDistance(fromRide, toRide) + " minute(s)");

                if (preference.getRideQueueTime() > user.getMaxWaitingTime()) {
                    System.out.println("Queue time for "
                            + toRide.getName()
                            + "(" + toRide.getId()
                            + ") is "
                            + preference.getRideQueueTime() + " minute(s). This is greater than your maximum waiting time of "
                            + user.getMaxWaitingTime() + " minute(s), therefore, "
                            + toRide.getName()
                            + "(" + toRide.getId()
                            + ") is skipped");

                } else {

                    totalCost = totalCost + dijkstra.getShortestDistance(fromRide, toRide);
                    LinkedList<Ride> myPath = dijkstra.getPath(toRide);
                    String pref = preference.getRide().getId();
                    if (queueTimesTable.get(pref) != null) {
                        totalQueueTime = totalQueueTime + queueTimesTable.get(pref);
                    }

                    boolean startPosition = true;

                    for (Ride step : myPath.items()) {
                        if (startPosition) {
                            System.out.println("Starting from '" + fromRide.getName() + "' (" + fromRide.getId() + ")");
                            startPosition = false;
                        } else {
                            System.out.println("Go To -> '" + step.getName() + "' (" + step.getId() + ")");
                        }
                        fromRide = toRide;
                    }

                }
            }

            totalCost = totalCost + totalQueueTime;

            if (totalCost == 0) {
                System.out.println("\nAll rides have been skipped, cost of journey is 0\n");
            } else {
                System.out.println("\nTotal cost of journey is " + totalCost + " minutes\n");
            }

            System.out.println("Would you like to go back to the start?\nEnter 'Y' to proceed\nEnter any other key to terminate the program");

            Scanner scan = new Scanner(System.in);

            String backToStart = scan.nextLine();

            backToStart = backToStart.toUpperCase();

            if (backToStart.equals("Y")) {
                exit = false;

                System.out.println("");
            } else {

                exit = true;

            }
        }
        System.out.println("\nThank you for using Thorpe Park Navigator!");

    }

    private void calculateDistance(Ride source, Ride destination) {
        visitedNodes = new HashSet<Ride>();
        unvisitedNodes = new HashSet<Ride>();
        distance = new HashMap<Ride, Integer>();
        eachStep = new HashMap<Ride, Integer>();
        predecessors = new HashMap<Ride, Ride>();
        distance.put(source, 0);
        unvisitedNodes.add(source);
        while (unvisitedNodes.size() > 0) {
            Ride ride = getMinimum(unvisitedNodes);
            visitedNodes.add(ride);
            unvisitedNodes.remove(ride);
            findMinimalDistances(ride, destination);
        }

    }

    private void findMinimalDistances(Ride ride, Ride destination) {
        List<Ride> adjacentNodes = getNeighbours(ride);
        for (Ride target : adjacentNodes) {
            int distanceToTarget = getShortestDistance(target);
            int distanceToRide = getShortestDistance(ride);
            int distanceFromRideToTarget = getDistance(ride, target);

            if (distanceToTarget > distanceToRide + distanceFromRideToTarget) {
                distance.put(target, distanceToRide + distanceFromRideToTarget);
                eachStep.put(target, distanceFromRideToTarget);
                predecessors.put(target, ride);
                unvisitedNodes.add(target);
            }
        }
    }

    private int getDistance(Ride source, Ride destination) {
        int dist = 0;
        for (Path path : paths) {
            if (path.getFromRide().equals(source) && path.getToRide().equals(destination)) {
                dist = path.getWeights();
                break;
            }
        }
        return dist;
    }

    private List<Ride> getNeighbours(Ride ride) {
        List<Ride> neighbours = new ArrayList<Ride>();
        for (Path path : paths) {
            if (path.getFromRide().equals(ride)
                    && !isVisited(path.getToRide())) {
                neighbours.add(path.getToRide());
            }
        }
        return neighbours;
    }

    private Ride getMinimum(Set<Ride> nodes) {
        Ride minimum = null;
        for (Ride node : nodes) {
            if (minimum == null) {
                minimum = node;
            } else {
                if (getShortestDistance(node) < getShortestDistance(minimum)) {
                    minimum = node;
                }
            }
        }
        return minimum;
    }

    private boolean isVisited(Ride node) {
        return visitedNodes.contains(node);
    }

    private int getShortestDistance(Ride source, Ride destination) {
        calculateDistance(source, destination);
        Integer weight = distance.get(destination);
        if (weight == null) {
            return Integer.MAX_VALUE;
        } else {
            return weight;
        }
    }

    private int getShortestDistance(Ride destination) {
        Integer weight = distance.get(destination);
        if (weight == null) {
            return Integer.MAX_VALUE;
        } else {
            return weight;
        }
    }

    public LinkedList<Ride> getPath(Ride target) {  //This method returns the path from the source to the selected target and NULL if no path exists
        LinkedList<Ride> path = null;
        path = new LinkedList<Ride>();
        Ride step = target;
        // check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        return path;
    }

    private Ride getRide(String rideId) {
        Ride tride = this.rides.stream()
                .filter(ride -> rideId.equalsIgnoreCase(ride.getId()))
                .findAny()
                .orElse(null);
        return tride;
    }

    private User getUser(String userName, List<User> users) {
        User tuser = users.stream()
                .filter(user -> userName.equalsIgnoreCase(user.getUserName()))
                .findAny()
                .orElse(null);
        return tuser;
    }

    private User validateUserAndPassword(String userName, String password, List<User> users) {
        User tuser = users.stream()
                .filter(user -> userName.equalsIgnoreCase(user.getUserName()))
                .filter(user -> password.equals(user.getPassword()))
                .findAny()
                .orElse(null);
        return tuser;
    }

    private static boolean isValidLocation(String location) {
        if (location.length() != 1 || !Character.isLetter(location.charAt(0))) {
            System.out.println("Invalid input. Please enter a single letter.");
            return false;
        } else {
            return true;
        }
    }
}
