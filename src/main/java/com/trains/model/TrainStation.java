package main.java.com.trains.model;

import java.util.ArrayList;
import java.util.List;

public class TrainStation extends DijkstraNode {
    private final String townName;
    private final List<Route> routesFromStation;

    public TrainStation(String name) {
        super();
        this.townName = name;
        this.routesFromStation = new ArrayList<Route>();
    }

    public String getTownName() {
        return townName;
    }

    public List<Route> getRoutesFromStation() {
        return routesFromStation;
    }

    public void addRoute(Route newRoute) {
        routesFromStation.add(newRoute);
    }

    @Override
    public String toString() {
        return "TrainStation{" +
                "townName='" + townName + '\'' +
                ", routesFromStation=" + routesFromStation +
                "} " + super.toString();
    }
}
