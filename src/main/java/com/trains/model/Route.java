package main.java.com.trains.model;

public class Route {
    private final String originStationTownName;
    private String destinyStationTownName;
    private final int distance;

    public Route(String originTown, String destinyTown, int distance) {
        this.originStationTownName = originTown;
        this.destinyStationTownName = destinyTown;
        this.distance = distance;
    }

    public String getOriginStationTownName() {
        return originStationTownName;
    }

    public String getDestinyStationTownName() {
        return destinyStationTownName;
    }

    public void setDestinyStationTownName(String destinyStationTownName) {
        this.destinyStationTownName = destinyStationTownName;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Route{" +
                "originStation='" + originStationTownName + '\'' +
                ", destinyStation='" + destinyStationTownName + '\'' +
                ", distance=" + distance +
                '}';
    }
}
