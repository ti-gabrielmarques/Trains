package main.java.com.trains.utils;

import main.java.com.trains.model.Railroad;

import java.util.Scanner;

public final class ManualInputReader {

    private ManualInputReader() {
    }

    public static boolean isManualInputInsertAcceptable(Scanner commandLineInput) {
        System.out.println("Input text file was not found under project resources. Would you like to continue the " +
                "application execution by informing the graph structure manually ? (y/n)");

        String userAnswer = commandLineInput.nextLine();

        if (userAnswer.toLowerCase().equals("y")) {
            return true;
        } else if (userAnswer.toLowerCase().equals("n")) {
            commandLineInput.close();
            return false;
        } else {
            System.out.println("The key informed is not acceptable");
            return isManualInputInsertAcceptable(commandLineInput);
        }
    }

    public static Railroad buildRailroad(Scanner commandLineInput) {
        Railroad railroad = new Railroad();

        System.out.println("Please, inform the first route information (Ex: AB3 = route from A to B with distance 3) " +
                "or type 'done' (without quotes) to leave railroad creation");
        String inputRoute = commandLineInput.nextLine();

        while (!inputRoute.toLowerCase().equals("done")) {

            if (isInputValid(inputRoute)) {
                railroad.addRouteToRailroad(inputRoute, railroad);

                System.out.println("Added successfully! Inform next route or type 'done' (without quotes) to leave railroad creation");
                inputRoute = commandLineInput.nextLine();
            } else {
                System.out.println("Informed route is invalid. Please, inform a valid route ");
            }
        }

        commandLineInput.close();
        return railroad;
    }

    private static boolean isInputValid(String inputRoute) {
        return Character.isLetter(inputRoute.charAt(0))
                && Character.isLetter(inputRoute.charAt(1))
                && Character.isDigit(inputRoute.charAt(2));
    }
}
