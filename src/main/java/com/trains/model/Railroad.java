package main.java.com.trains.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Railroad {
    private final List<TrainStation> trainStations;

    private static int numberOfRoutesFound = 0;
    public static final String NO_SUCH_ROUTE = "NO SUCH ROUTE";
    public static final String TEMP = "Temp";

    public Railroad() {
        this.trainStations = new ArrayList<>();
    }

    public List<TrainStation> getTrainStations() {
        return trainStations;
    }

    public void addTrainStation(TrainStation trainStation) {
        trainStations.add(trainStation);
    }

    public void addRouteToRailroad(String inputRoute, Railroad railroad) {

        String originTrainStationTownName = String.valueOf(inputRoute.charAt(0));
        String destinyTrainStationTownName = String.valueOf(inputRoute.charAt(1));
        int distanceFromTrainStations = Integer.parseInt(String.valueOf(inputRoute.charAt(2)));

        createTrainStationIfNotPresent(railroad, originTrainStationTownName);
        createTrainStationIfNotPresent(railroad, destinyTrainStationTownName);
        createRouteBetweenTrainStations(railroad, originTrainStationTownName, destinyTrainStationTownName,
                distanceFromTrainStations);
    }

    public String calculateRouteDistance(String trainStationTownNames) {
        if (trainStationTownNames.length() <= 1) {
            return NO_SUCH_ROUTE;
        }

        String[] townNamesInRouteOrder = trainStationTownNames.split("");

        int distance = 0;
        for (int originTownIndex = 0; originTownIndex < townNamesInRouteOrder.length - 1; originTownIndex++) {
            String originTownName = townNamesInRouteOrder[originTownIndex];

            int destinyTownIndex = originTownIndex + 1;
            String destinyTownName = townNamesInRouteOrder[destinyTownIndex];

            Route relatedRoute;
            try {
                relatedRoute = getRouteByTownNames(originTownName, destinyTownName);
            } catch (NullPointerException e) {
                relatedRoute = null;
            }

            if (relatedRoute == null) {
                return NO_SUCH_ROUTE;
            }

            distance += getRouteByTownNames(originTownName, destinyTownName).getDistance();
        }

        return String.valueOf(distance);
    }

    public String getNumberOfTripsFromTo(String originTownName, String destinyTownName, int numberOfStops, boolean isExactStopsRequired) {
        if (numberOfStops <= 1) {
            return "0";
        }

        if (getTrainStationByTownName(originTownName) == null || getTrainStationByTownName(destinyTownName) == null) {
            return "One or both towns are not present on train station register";
        }

        TrainStation originTrainStation = getTrainStationByTownName(originTownName);

        List<TrainStationTree> railroadDepthPath = new ArrayList<>();
        recursivelyCalculateNumberOfPossibleTrips(originTrainStation,
                numberOfStops, 0, railroadDepthPath);

        Stream<TrainStationTree> allDestinyTrainStationsOccurrences = railroadDepthPath.stream()
                .filter(trainStationTreeNode -> trainStationTreeNode.getDepth() != 0)
                .filter(trainStationTreeNode -> trainStationTreeNode.getTrainStationName().equals(destinyTownName));

        if (isExactStopsRequired) {
            return String.valueOf(allDestinyTrainStationsOccurrences
                    .filter(trainStationTreeNode -> trainStationTreeNode.getDepth() == numberOfStops)
                    .count());
        } else {
            return String.valueOf(allDestinyTrainStationsOccurrences
                    .count());
        }
    }

    /*
    Implements customized Dijkstra algorithm
     */
    public String getShortestRouteDistance(String originTownName, String destinyTownName) {
        if (getTrainStationByTownName(originTownName) == null || getTrainStationByTownName(destinyTownName) == null) {
            return "One or both towns are not present on train station register";
        }

        if (originTownName.equals(destinyTownName)) {
            createTemporaryTrainStationToSimulateCycle(getTrainStationByTownName(originTownName));
            destinyTownName = TEMP;
        }

        Set<TrainStation> settledTrainStations = new HashSet<>();
        Set<TrainStation> unsettledTrainStations = new HashSet<>();

        TrainStation originTrainStation = getTrainStationByTownName(originTownName);
        cleanDistanceAttributeForNewCalculation(originTrainStation);

        unsettledTrainStations.add(originTrainStation);

        while (unsettledTrainStations.size() != 0) {
            TrainStation currentTrainStation = getLowestDistanceTrainStation(unsettledTrainStations);
            unsettledTrainStations.remove(currentTrainStation);

            for (Route route : currentTrainStation.getRoutesFromStation()) {
                TrainStation routeDestinyTrainStation = getTrainStationByTownName(route.getDestinyStationTownName());

                int routeDistance = getRouteByTownNames(currentTrainStation.getTownName(), routeDestinyTrainStation.getTownName()).getDistance();

                if (!settledTrainStations.contains(routeDestinyTrainStation)) {
                    calculateMinimumDistance(routeDestinyTrainStation,
                            getTrainStationByTownName(route.getOriginStationTownName()),
                            routeDistance);
                    unsettledTrainStations.add(routeDestinyTrainStation);
                }
            }
            settledTrainStations.add(currentTrainStation);
        }

        if (destinyTownName.equals(TEMP)) {
            String shortestPathDistance = String.valueOf(getTrainStationByTownName(destinyTownName).getDistance());
            deleteTemporaryTrainStation();

            return shortestPathDistance;
        }

        return String.valueOf(getTrainStationByTownName(destinyTownName).getDistance());
    }

    public String getNumberOfDifferentRoutesFromTo(String originTownName,
                                                   String destinyTownName,
                                                   int distanceLimit) {
        Railroad.numberOfRoutesFound = 0;

        getNumberOfDifferentRoutesFromTo(originTownName, destinyTownName, distanceLimit, originTownName);

        return String.valueOf(Railroad.numberOfRoutesFound);
    }

    private void getNumberOfDifferentRoutesFromTo(String originTownName,
                                                  String destinyTownName,
                                                  int distanceLimit,
                                                  String trainStationPathTownNames) {
        for (Route route : getTrainStationByTownName(originTownName).getRoutesFromStation()) {
            String nextPath = trainStationPathTownNames + route.getDestinyStationTownName();
            int currentDistance = Integer.parseInt(calculateRouteDistance(nextPath));

            if (destinyTownName.equals(route.getDestinyStationTownName()) && (currentDistance < distanceLimit)) {
                Railroad.numberOfRoutesFound++;
            }

            if (currentDistance < distanceLimit) {
                getNumberOfDifferentRoutesFromTo(route.getDestinyStationTownName(), destinyTownName, distanceLimit, nextPath);
            }
        }
    }

    private void deleteTemporaryTrainStation() {
        TrainStation tempTrainStation = getTrainStationByTownName(TEMP);
        getAllStationsThatHasRoutesToDestinyStation(tempTrainStation).forEach(trainStation ->
                trainStation.getRoutesFromStation().removeIf(route -> route.getDestinyStationTownName().equals(TEMP)));

        getTrainStations().removeIf(trainStation -> trainStation.getTownName().equals(TEMP));
    }

    private void createTemporaryTrainStationToSimulateCycle(TrainStation stationToFindItsShortestCycle) {
        TrainStation temporaryTrainStation = new TrainStation(TEMP);

        getAllStationsThatHasRoutesToDestinyStation(stationToFindItsShortestCycle).forEach(trainStation -> {
            Route existingRouteToDestinyStation = trainStation.getRoutesFromStation().stream()
                    .filter(route -> route.getDestinyStationTownName().equals(stationToFindItsShortestCycle.getTownName()))
                    .findFirst()
                    .orElse(null);
            if (existingRouteToDestinyStation != null) {
                Route newTemporaryRoute = new Route(existingRouteToDestinyStation.getOriginStationTownName(), TEMP, existingRouteToDestinyStation.getDistance());
                trainStation.getRoutesFromStation().add(newTemporaryRoute);
            }
        });

        getTrainStations().add(temporaryTrainStation);
    }

    private List<TrainStation> getAllStationsThatHasRoutesToDestinyStation(TrainStation stationToFindItsShortestCycle) {
        return getTrainStations().stream()
                .filter(trainStation -> hasRoutesFrom(stationToFindItsShortestCycle, trainStation))
                .collect(Collectors.toList());
    }

    private boolean hasRoutesFrom(TrainStation stationToFindItsShortestCycle, TrainStation trainStation) {
        for (Route route : trainStation.getRoutesFromStation()) {
            if (route.getDestinyStationTownName().equals(stationToFindItsShortestCycle.getTownName())) {
                return true;
            }
        }

        return false;
    }

    private void cleanDistanceAttributeForNewCalculation(TrainStation originTrainStation) {
        getTrainStations().forEach(trainStation -> trainStation.setDistance(Integer.MAX_VALUE));
        originTrainStation.setDistance(0);
    }

    private TrainStation getLowestDistanceTrainStation(Set<TrainStation> unsettledTrainStations) {
        TrainStation closestTrainStation = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (TrainStation trainStation : unsettledTrainStations) {
            int trainStationDistance = trainStation.getDistance();

            if (trainStationDistance <= lowestDistance) {
                lowestDistance = trainStationDistance;
                closestTrainStation = trainStation;

            }
        }
        return closestTrainStation;
    }

    private void calculateMinimumDistance(TrainStation currentTrainStation,
                                          TrainStation originTrainStation,
                                          int routeDistance) {
        int distanceFromOrigin = originTrainStation.getDistance();
        if (distanceFromOrigin + routeDistance < currentTrainStation.getDistance()) {
            currentTrainStation.setDistance(distanceFromOrigin + routeDistance);
            LinkedList<TrainStation> shortestPath = new LinkedList<>(originTrainStation.getShortestPath());
            shortestPath.add(originTrainStation);
            currentTrainStation.setShortestPath(shortestPath);
        }
    }

    private TrainStation getTrainStationByTownName(String originTownName) {
        return trainStations.stream()
                .filter(trainStation -> trainStation.getTownName().equals(originTownName))
                .findFirst()
                .orElse(null);
    }

    private void recursivelyCalculateNumberOfPossibleTrips(TrainStation originTrainStation,
                                                           int maximumNumberOfStops,
                                                           int currentDepth,
                                                           List<TrainStationTree> railroadDepthPath) {
        railroadDepthPath.add(new TrainStationTree(originTrainStation.getTownName(), currentDepth));

        Set<Route> possibleRoutes = new HashSet<>(originTrainStation.getRoutesFromStation());

        if (possibleRoutes.isEmpty() || currentDepth == maximumNumberOfStops) {
            return;
        }

        possibleRoutes.forEach(route -> {
            TrainStation newOriginTrainStation = getTrainStationByTownName(route.getDestinyStationTownName());
            recursivelyCalculateNumberOfPossibleTrips(newOriginTrainStation, maximumNumberOfStops,
                    currentDepth + 1, railroadDepthPath);
        });
    }

    private static void createTrainStationIfNotPresent(Railroad railroad, String trainStationTownName) {
        if (!railroad.hasTrainStationRegistered(trainStationTownName)) {
            TrainStation newTrainStation = new TrainStation(trainStationTownName);
            railroad.addTrainStation(newTrainStation);
        }
    }

    private boolean hasTrainStationRegistered(String trainStationTownName) {
        if (trainStations.isEmpty()) {
            return false;
        }

        return trainStations.stream()
                .anyMatch(trainStation -> trainStation.getTownName().equals(trainStationTownName));
    }

    private static void createRouteBetweenTrainStations(Railroad railroad, String originTrainStationTownName,
                                                        String destinyTrainStationTownName, int distanceFromTrainStations) {
        railroad.getTrainStations().stream()
                .filter(trainStation -> trainStation.getTownName().equals(originTrainStationTownName))
                .findAny()
                .ifPresent(originTrainStation -> createAndAddNewRouteToOriginTrainStation(originTrainStationTownName,
                        destinyTrainStationTownName, distanceFromTrainStations, originTrainStation));

    }

    private static void createAndAddNewRouteToOriginTrainStation(String originTrainStationTownName,
                                                                 String destinyTrainStationTownName,
                                                                 int distanceFromTrainStations,
                                                                 TrainStation originTrainStation) {
        Route newRoute = new Route(originTrainStationTownName, destinyTrainStationTownName, distanceFromTrainStations);
        originTrainStation.addRoute(newRoute);
    }

    private Route getRouteByTownNames(String originTownName, String destinyTownName) {
        return Objects.requireNonNull(getTrainStationByTownName(originTownName))
                .getRoutesFromStation().stream()
                .filter(routeFromOrigin -> routeFromOrigin.getDestinyStationTownName().equals(destinyTownName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return "Railroad{" +
                "trainStations=" + trainStations +
                '}';
    }
}
