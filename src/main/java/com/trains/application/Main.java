package main.java.com.trains.application;

import main.java.com.trains.model.Railroad;
import main.java.com.trains.utils.ManualInputReader;
import main.java.com.trains.utils.TxtFileInputReader;

import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Optional<Railroad> railroadOptional = buildRailroad();

        if (railroadOptional.isEmpty()) {
            throw new RuntimeException("No railroad/graph were correctly created previously. Please try populating " +
                    "the railroad again from scratch in a new application execution");
        }

        Railroad railroad = railroadOptional.get();

        System.out.println("Output #1: " + railroad.calculateRouteDistance("ABC"));
        System.out.println("Output #2: " + railroad.calculateRouteDistance("AD"));
        System.out.println("Output #3: " + railroad.calculateRouteDistance("ADC"));
        System.out.println("Output #4: " + railroad.calculateRouteDistance("AEBCD"));
        System.out.println("Output #5: " + railroad.calculateRouteDistance("AED"));
        System.out.println("Output #6: " + railroad.getNumberOfTripsFromTo("C", "C", 3, false));
        System.out.println("Output #7: " + railroad.getNumberOfTripsFromTo("A", "C", 4, true));
        System.out.println("Output #8: " + railroad.getShortestRouteDistance("A", "C"));
        System.out.println("Output #9: " + railroad.getShortestRouteDistance("B", "B"));
        System.out.println("Output #10: " + railroad.getNumberOfDifferentRoutesFromTo("C", "C", 30));

    }

    private static Optional<Railroad> buildRailroad() {
        try {
            return Optional.of(TxtFileInputReader.buildRailroad());
        } catch (FileNotFoundException e) {
            Scanner commandLineInput = new Scanner(System.in);

            if (ManualInputReader.isManualInputInsertAcceptable(commandLineInput)) {
                return Optional.of(ManualInputReader.buildRailroad(commandLineInput));
            }

            commandLineInput.close();
        }
        return Optional.empty();
    }
}
