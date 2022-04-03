package main.java.com.trains.model;

import java.util.LinkedList;
import java.util.List;

public class DijkstraNode {
    private List<TrainStation> shortestPath;
    private Integer distance;

    public DijkstraNode() {
        this.shortestPath = new LinkedList<>();
        this.distance = Integer.MAX_VALUE;
    }

    public List<TrainStation> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(List<TrainStation> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }
}
