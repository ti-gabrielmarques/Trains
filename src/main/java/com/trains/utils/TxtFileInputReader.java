package main.java.com.trains.utils;

import main.java.com.trains.model.Railroad;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public final class TxtFileInputReader {

    private TxtFileInputReader() {
    }

    public static Railroad buildRailroad() throws FileNotFoundException {
        Scanner inputFile = new Scanner(new FileReader("src/main/java/resources/input.txt"));

        // Ignore first word ("Graph:") from input file
        inputFile.next();

        Railroad railroad = new Railroad();

        while (inputFile.hasNext()) {
            railroad.addRouteToRailroad(inputFile.next(), railroad);
        }

        return railroad;
    }
}
