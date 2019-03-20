package com.akeneo.uriel.PyramidPathsCalculator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Calculator
{
    static private final String                 PYRAMID_INPUT_FILE = "input.txt";
    static private HashMap<Integer, Integer>    pathsSumsWithOccurrences = new HashMap<>();
    private List<String>                        linesOfPyramid = null;


    private Calculator() {
    }

    private void validatePyramid() throws Exception {

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(PYRAMID_INPUT_FILE))) {

            String currentLine;
            int amountOfNumbersInLine = 0;

            while ((currentLine = bufferedReader.readLine()) != null) {

                String[] values = currentLine.split(" ");

                // Pyramid structure check:
                if (values.length-amountOfNumbersInLine != 1)
                    throw new Exception("Pyramid structure is not valid.");

                amountOfNumbersInLine++;

                // Values check:
                // If the value isn't parsable, Exception will be thrown.
                for (String value : values)
                    Integer.parseInt(value);

            }

        }

    }

    private void getLines() throws IOException {

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(PYRAMID_INPUT_FILE))) {

            List<String> lines = new ArrayList<>();
            String currentLine;

            // Saving lines for later usage:
            while ((currentLine = bufferedReader.readLine()) != null) {
                lines.add(currentLine);
            }

            linesOfPyramid = lines;
        }


    }

    private void calculate (int currentLineInPyramid, int currentPositionInLine, int currentSum){

        if (currentLineInPyramid == linesOfPyramid.size()) {
            return;
        }

        // In case the bottom of the pyramid is not reached:
        String line = linesOfPyramid.get(currentLineInPyramid);
        String[] values = line.split(" ");
        int currentValue = Integer.parseInt(values[currentPositionInLine]);

        currentSum+= currentValue;

        if (currentLineInPyramid == linesOfPyramid.size()-1) {
            // When the program reach the bottom of the pyramid.
            // It will automatically fill the current sum into the HashMap.
            if (pathsSumsWithOccurrences.containsKey(currentSum))
                pathsSumsWithOccurrences.put(currentSum, pathsSumsWithOccurrences.get(currentSum) + 1);
            else
                pathsSumsWithOccurrences.put(currentSum, 1);
        }

        else {
            // Recursive calls to sum all paths combinations:

            // Left side:
            calculate(currentLineInPyramid + 1, currentPositionInLine, currentSum);
            // Right side:
            calculate(currentLineInPyramid + 1, currentPositionInLine + 1, currentSum);
        }


    }

    private static List<Map.Entry<Integer, Integer>> sortHashMapByValueIntoList(HashMap<Integer, Integer> data){
        // Create a list from elements of HashMap
        List<Map.Entry<Integer, Integer> > list = new LinkedList<>(data.entrySet());

        // Sort the list
        list.sort(new Comparator<Map.Entry<Integer, Integer>>()
        {
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return (o2.getValue()) - (o1.getValue());
            }
        });

        return list;
    }

    private static void printPyramidCalculation(){

        List<Map.Entry<Integer, Integer>> orderedHashMapInList =
                sortHashMapByValueIntoList(pathsSumsWithOccurrences);

        System.out.printf("%-5s | %-5s %n","Sum","Count");
        for (Map.Entry<Integer, Integer> entry: orderedHashMapInList){
            System.out.printf("%-5d | %-5d %n",entry.getKey(), entry.getValue());
        }

    }

    public static void calculatePyramidAndPrint(){
        Calculator pyramidCalculator = new Calculator();

        try {
            pyramidCalculator.validatePyramid();
            pyramidCalculator.getLines();
            pyramidCalculator.calculate(0, 0, 0);
            printPyramidCalculation();
        }

        // If value in the pyramid is not parsable:
        catch (NumberFormatException e){
            System.out.println("One of the pyramid values is not numeric!");
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

}
