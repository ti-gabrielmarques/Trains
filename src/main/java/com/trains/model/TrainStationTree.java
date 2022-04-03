package main.java.com.trains.model;

public class TrainStationTree {
    private final String trainStationName;
    private final int depth;

    public TrainStationTree(String trainStationName, int depth) {
        this.trainStationName = trainStationName;
        this.depth = depth;
    }

    public String getTrainStationName() {
        return trainStationName;
    }

    public int getDepth() {
        return depth;
    }
}
