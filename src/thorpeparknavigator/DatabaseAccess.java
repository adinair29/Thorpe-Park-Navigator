/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thorpeparknavigator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aditya nair
 */
public class DatabaseAccess {

    public Connection databaseConnect() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");//Specifying the SQLite Java driver
            conn = DriverManager.getConnection("jdbc:sqlite:C://Users//adima//Downloads//TP13012023//ThorpeParkNavigator//ThorpeParkNavigatorDatabase.db");//Specify the database, since relative in the main project folder
            conn.setAutoCommit(false);// Controls when data is written
            System.out.println("\nOpened database successfully\n");
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    public void close(Connection conn) {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createUser(Connection conn, String userName, String password) {
        String insertsql = "INSERT INTO User (UserID, Username, Password,"
                + " UserCurrentLocation , MaxWaitingTime ) "
                + "VALUES (?,? ,? , ?, ?);";
        try {
            PreparedStatement ps = conn.prepareStatement(insertsql);
            ps.setString(2, userName);
            ps.setString(3, password);
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(DijkstraAlgo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void saveQueueTime(Connection conn, HashMap<String, Integer> qTimesTable) {
        
        String insertsql = "REPLACE INTO QueueTimes "
                + "(RideID, QueueTime) VALUES (?,?) ";
        
        for (String rideId : qTimesTable.keySet()) {
            try {
                int qTimeId = 0;
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("Select * from QueueTimes where RideID ='" + rideId +"'");
                if (rs.next()) { //Ride exits, so update it 
                    qTimeId = rs.getInt("QueueTimeID");
                    insertsql = "REPLACE INTO QueueTimes "
                            + "(QueueTimeID, RideID, QueueTime) VALUES (?,?,?) ";
                    PreparedStatement ps = conn.prepareStatement(insertsql);
                    ps.setInt(1, qTimeId);
                    ps.setString(2, rideId);
                    ps.setInt(3, qTimesTable.get(rideId));
                    ps.executeUpdate();
                    ps.close();
                } else {
                    insertsql = "REPLACE INTO QueueTimes "
                            + "(RideID, QueueTime) VALUES (?,?) ";
                    PreparedStatement ps = conn.prepareStatement(insertsql);
                    ps.setString(1, rideId);
                    ps.setInt(2, qTimesTable.get(rideId));
                    ps.executeUpdate();
                    ps.close();
                }
                conn.commit();
            } catch (SQLException ex) {
                Logger.getLogger(DijkstraAlgo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void savePreferences(Connection conn, String userId, String currentLocationOfUser,
            Integer maxWaitingTime, HashMap<String, Integer> preferences) {

        String updateSQL = "UPDATE User set UserCurrentLocation = ? , MaxWaitingTime = ?"
                + " where UserID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(updateSQL);
            ps.setString(1, currentLocationOfUser);
            ps.setInt(2, maxWaitingTime);
            ps.setString(3, userId);
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (String rideId : preferences.keySet()) {

            try {
                String insertsql = "INSERT OR IGNORE INTO Preferences "
                        + "( PreferenceID, UserID , RideID, PriorityOrder ) VALUES ( ?,?,?,? ) ";
                PreparedStatement ps = conn.prepareStatement(insertsql);
                ps.setString(2, userId);
                ps.setString(3, rideId);
                ps.setInt(4, preferences.get(rideId));
                ps.executeUpdate();
                conn.commit();

            } catch (SQLException ex) {
                Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public boolean createTable(Connection conn) {
        Statement stmt = null;
        boolean returnValue = false;
        try {
            stmt = conn.createStatement();
            String dropRides = "DROP TABLE if exists Rides";
            stmt.executeUpdate(dropRides);
            String dropRideEdges = "DROP TABLE if exists RideEdges";
            stmt.executeUpdate(dropRideEdges);
//            String dropUser = "DROP TABLE if exists User";
//            stmt.executeUpdate(dropUser);
            String dropPreferences = "DROP TABLE if exists Preferences";
            stmt.executeUpdate(dropPreferences);
//            String dropQueueTimes = "DROP TABLE if exists QueueTimes";
//            stmt.executeUpdate(dropQueueTimes);
            conn.commit();

            String sql1 = "CREATE TABLE Rides"
                    + " (RideID VARCHAR(255) PRIMARY KEY     NOT NULL,"
                    + " RideName VARCHAR(255))";

            stmt.executeUpdate(sql1);
            conn.commit();

            Map<String, String> ridesTable = new HashMap<String, String>();

            ridesTable.put("A", "Entrance");
            ridesTable.put("B", "Depth Charge");
            ridesTable.put("C", "Vortex");
            ridesTable.put("D", "Quantum");
            ridesTable.put("E", "Flying fish");
            ridesTable.put("F", "Zodiac");
            ridesTable.put("G", "Rush");
            ridesTable.put("H", "Colossus");
            ridesTable.put("I", "The Walking Dead: The Ride");
            ridesTable.put("J", "Ghost Train");
            ridesTable.put("K", "Samurai");
            ridesTable.put("L", "Saw - The Ride");
            ridesTable.put("M", "Storm Surge");
            ridesTable.put("N", "The Swarm");
            ridesTable.put("O", "Tidal Wave");
            ridesTable.put("P", "Dodgems");
            ridesTable.put("Q", "Detonator");
            ridesTable.put("R", "Stealth");
            ridesTable.put("S", "Storm in a Tea Cup");
            ridesTable.put("T", "Nemesis Inferno");
            ridesTable.put("U", "Rumba Rapids");

            for (String rideCode : ridesTable.keySet()) {
                String rideDescription = (String) ridesTable.get(rideCode);

                String insertsql = "INSERT INTO Rides (RideID,RideName) "
                        + "VALUES (?,? );";

                PreparedStatement ps = conn.prepareStatement(insertsql);
                ps.setString(1, rideCode);
                ps.setString(2, rideDescription);
                ps.executeUpdate();
            }

            conn.commit();
            //System.out.println("Rides table created.");

            String sql2 = "CREATE TABLE IF NOT EXISTS RideEdges"
                    + "(EdgeID INTEGER PRIMARY KEY NOT NULL,"
                    + "FromRide VARCHAR(255) REFERENCES Rides(RideID),"
                    + "ToRide VARCHAR(255) REFERENCES Rides(RideID),"
                    + "WeightValue INTEGER NOT NULL)";

            String sql3 = "CREATE TABLE IF NOT EXISTS User"
                    + "(UserID INTEGER PRIMARY KEY NOT NULL,"
                    + "Username VARCHAR(255),"
                    + "Password VARCHAR(255),"
                    + "UserCurrentLocation VARCHAR(255) REFERENCES Rides(RideID),"
                    + "MaxWaitingTime INTEGER)";

            String sql4 = "CREATE TABLE IF NOT EXISTS Preferences"
                    + "(PreferenceID INTEGER PRIMARY KEY NOT NULL,"
                    + "UserID INTEGER REFERENCES User(UserID),"
                    + "RideID VARCHAR(255) REFERENCES Rides(RideID),"
                    + "PriorityOrder INTEGER)";

            String sql5 = "CREATE TABLE IF NOT EXISTS QueueTimes"
                    + "(QueueTimeID INTEGER PRIMARY KEY NOT NULL,"
                    + "RideID VARCHAR(255) REFERENCES Rides(RideID),"
                    + "QueueTime INTEGER)";

            stmt.executeUpdate(sql2);
            //System.out.println("RideEdges table created.");

            BufferedReader input;
            try {
                input = new BufferedReader(new FileReader(new File("C:\\Users\\adima\\Downloads\\TP13012023\\ThorpeParkNavigator\\\\DistanceBetweenRides.csv")));

                String line = null;
                String[] st = null;

                try {
                    while ((line = input.readLine()) != null) {
                        st = line.replace("\"", "").split(",");
                        if (!st[0].equals("EdgeId")) {
                            String insertsql = "INSERT INTO RideEdges (EdgeId,FromRide,ToRide,WeightValue) "
                                    + "VALUES (?,?,?,? );";

                            PreparedStatement ps = conn.prepareStatement(insertsql);
                            ps.setString(1, st[0]);
                            ps.setString(2, st[1]);
                            ps.setString(3, st[2]);
                            int distance = Integer.valueOf(st[3]);
                            ps.setInt(4, distance);
                            ps.executeUpdate();
                            conn.commit();
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(DatabaseAccess.class.getName()).log(Level.SEVERE, null, ex);
            }

            stmt.executeUpdate(sql3);
            conn.commit();
            stmt.executeUpdate(sql4);
            conn.commit();
            stmt.executeUpdate(sql5);
            conn.commit();
            stmt.close();
            returnValue = true;
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return returnValue;
    }

    public List< Ride> getRidesFromDB(Connection conn) {
        Statement stmt = null;
        ResultSet rs = null;
        List<Ride> nodeList = new ArrayList< Ride>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from Rides");
            while (rs.next()) {
                String rideId = rs.getString("RideID");
                String description = rs.getString("RideName");
                Ride ride = new Ride(rideId, description);
                nodeList.add(ride);
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return nodeList;
    }

    public List< Path> getEdgesFromDB(Connection conn, List<Ride> rides) {
        Statement stmt = null;
        ResultSet rs = null;
        List<Path> edgeList = new ArrayList< Path>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from RideEdges");
            while (rs.next()) {
                int id = rs.getInt("EdgeId");
                String fromRideId = rs.getString("FromRide");
                String toRideId = rs.getString("ToRide");
                int distance = rs.getInt("WeightValue");

                Ride sourceRide = rides.stream()
                        .filter(ride -> fromRideId.equals(ride.getId()))
                        .findAny()
                        .orElse(null);

                Ride destRide = rides.stream()
                        .filter(ride -> toRideId.equals(ride.getId()))
                        .findAny()
                        .orElse(null);

                Path edge = new Path(id, sourceRide, destRide, distance);
                edgeList.add(edge);
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return edgeList;
    }

    public List< User> getUsersFromDB(Connection conn) {
        Statement stmt = null;
        ResultSet rs = null;
        List<User> userList = new ArrayList< User>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from User");
            while (rs.next()) {
                String userId = rs.getString("UserId");
                String userName = rs.getString("UserName");
                String password = rs.getString("Password");
                String currentLocation = rs.getString("UserCurrentLocation");
                Integer maxWaitingTime = rs.getInt("MaxWaitingTime");
                User user = new User(userId, userName, password, currentLocation,
                        maxWaitingTime);
                userList.add(user);
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return userList;
    }

    public List< Preferences> getPreferenceForUserFromDB(Connection conn, List<Ride> rides, User user, String userId) {
        Statement stmt = null;
        ResultSet rs = null;
        List<Preferences> preferencesList = new ArrayList< Preferences>();

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from Preferences where UserID='" + userId + "' order by PriorityOrder asc");
            while (rs.next()) {

                int id = rs.getInt("PreferenceID");
                String rideId = rs.getString("RideID");
                int priorityOrder = rs.getInt("PriorityOrder");

                //Get wait time for the ride
                Statement rideWaitstmt = conn.createStatement();
                int rideQueueTime = 0;
                ResultSet rideWaitrs = rideWaitstmt.executeQuery("select * from QueueTimes where RideID='" + rideId + "'");
                if (rideWaitrs.next()) {
                    rideQueueTime = rideWaitrs.getInt("QueueTime");
                }

                Ride toRide = rides.stream()
                        .filter(ride -> rideId.equals(ride.getId()))
                        .findAny()
                        .orElse(null);

                Preferences preferences = new Preferences(id, user, toRide, priorityOrder, rideQueueTime);
                preferencesList.add(preferences);
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return preferencesList;
    }

}
