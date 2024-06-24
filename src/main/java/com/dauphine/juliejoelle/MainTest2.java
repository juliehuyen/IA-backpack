package com.dauphine.juliejoelle;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainTest2 {
    final static int NB_ITEMS = 6;
    final static int POPULATION_SIZE = 25;
    final static int NB_GENERATIONS = 50;
    final static double MUTATION_RATE = 0.2;
    final static double ELITIST_RATE = 0.1;
    final static int NB_ITERATIONS = 100;


    public static void main(String[] args) {
        List<Item> items = new ArrayList<>();
        List<Integer> utilities = Arrays.asList(
                560, 1125, 300, 620, 2100, 431, 68, 328, 47, 122, 322, 196, 41, 25, 425, 4260,
                416, 115, 82, 22, 631, 132, 420, 86, 42, 103, 215, 81, 91, 26, 49, 420,
                316, 72, 71, 49, 108, 116, 90, 738, 1811, 430, 3060, 215, 58, 296, 620, 418, 47, 81
        );
        List<List<Integer>> costsList = Arrays.asList(
                Arrays.asList(40,91, 10, 30, 160, 20, 3, 12, 3, 18, 9, 25, 1, 1, 10, 280, 10, 8, 1, 1, 49, 8, 21, 6, 1, 5 ,10, 8, 2, 1, 0, 10, 42, 6, 4, 8, 0, 10, 1, 40, 86, 11, 120, 8, 3, 32, 28, 13, 2 ,4),
                Arrays.asList(16,92, 41, 16, 150, 23, 4, 18, 6, 0, 12, 8, 2, 1, 0, 200, 20, 6, 2, 1, 70, 9, 22, 4, 1, 5, 10, 6, 4, 0, 4, 12, 8, 4, 3, 0, 10, 0, 6, 28, 93 ,9, 30, 22, 0, 36, 45, 13 ,2, 2),
                Arrays.asList(38, 39, 32, 71, 80, 26, 5, 40, 8, 12, 30, 15, 0 ,1, 23 ,100 ,0 ,20, 3 ,0, 40, 6 ,8 ,0, 6, 4 ,22 ,4 ,6 ,1 ,5 ,14 ,8 ,2 ,8 ,0 ,20 ,0 ,0 ,6 ,12, 6, 80 ,13, 6 ,22, 14, 0, 1 ,2),
                Arrays.asList(8, 71 ,30 ,60, 200 ,18, 6, 30, 4 ,8 ,31, 6 ,3 ,0, 18, 60, 21 ,4 ,0 ,2, 32 ,15 ,31, 2 ,2, 7, 8, 2, 8 ,0 ,2 ,8 ,6, 7 ,1, 0, 0, 20, 8 ,14, 20, 2, 40, 6 ,1 ,14, 20 ,12, 0 ,1),
                Arrays.asList(38, 52, 30, 42, 170, 9, 7, 20, 0, 3, 21, 4, 1, 2, 14, 310, 8, 4, 6, 1, 18, 15, 38, 10, 4, 8, 6, 0, 0, 3, 0, 10, 6, 1, 3, 0, 3, 5, 4, 0, 30, 12, 16, 18, 3, 16, 22, 30, 4, 0)
        );

        for (int i = 0; i < utilities.size(); i++) {
            items.add(new Item(utilities.get(i), costsList.get(i % costsList.size())));
        }

        // Define budgets
        List<Integer> budgets = Arrays.asList(800, 650, 550, 550, 650);
//        List<Item> items = new ArrayList<>();
//        List<Integer> utilities = Arrays.asList(100, 600, 1200, 2400, 500, 2000);
//        List<List<Integer>> costsList = Arrays.asList(
//                Arrays.asList(8,8,3,5,5,5,0,3,3,3),
//                Arrays.asList(12,12,6,10,13,13,0,0,2,2),
//                Arrays.asList(13,13,4,8,8,8,0,4,4,4),
//                Arrays.asList(64,75,18,32,42,48,0,8,8,8),
//                Arrays.asList(22,22,6,6,6,6,8,0,0,8),
//                Arrays.asList(41,41,4,12,20,20,0,0,4,4)
//        );
//
//        for (int i = 0; i < NB_ITEMS; i++) {
//            items.add(new Item(utilities.get(i), costsList.get(i)));
//        }
//
//        // Define budgets
//        List<Integer> budgets = Arrays.asList(80, 96, 20, 36, 44, 48, 10, 18, 22, 24);

        // Create backpack
        Backpack backpack = new Backpack(budgets, items);

        // Initialize and execute the genetic algorithm
        GeneticAlgorithm ga = new GeneticAlgorithm(backpack, items, POPULATION_SIZE, NB_GENERATIONS);

        HashMap<String, List<Double>> moyennesMap = new HashMap<>();
        HashMap<String, List<Double>> ecartTypesMap = new HashMap<>();

        // Solve the problem
        List<String> selections = Arrays.asList("selection", "tournament");
        List<String> mutations = Arrays.asList("mutation", "swap");
        List<String> crossovers = Arrays.asList("crossover", "onepoint","multipoints");
        List<String> repairs = Arrays.asList("repair", "weight");
        /******** POUR TESTER TOUS LES VARIANTS ********/
//        for (String s : selections) {
//            for (String m : mutations) {
//                for (String c : crossovers) {
//                    for (String r : repairs) {
//                        List<List<Backpack>> backpacks = new ArrayList<>();
//                        for (int i = 0; i < NB_ITERATIONS; i++) {
//                            List<Backpack> solutions = ga.solveVariant(s, m, c, r, MUTATION_RATE, ELITIST_RATE);
//                            backpacks.add(solutions);
//                        }
//                        List<Double> moyennes = new ArrayList<>();
//                        List<Double> ecartTypes = new ArrayList<>();
//                        for (int g = 0; g < NB_GENERATIONS; g++) {
//                            int moyenne = 0;
//                            double ecartType = 0;
//                            for (int j = 0; j < NB_ITERATIONS; j++) {
//                                Backpack b = backpacks.get(j).get(g);
//                                moyenne += b.getFitness();
//                            }
//                            moyenne = moyenne / NB_ITERATIONS;
//                            moyennes.add((double) moyenne);
//                            for (int k = 0; k < NB_ITERATIONS; k++) {
//                                Backpack b = backpacks.get(k).get(g);
//                                ecartType += Math.pow((b.getFitness() - moyenne), 2);
//                            }
//                            ecartType = Math.sqrt(ecartType / NB_ITERATIONS);
//                            ecartTypes.add(ecartType);
//                            System.out.println("Selection : " + s + ", Mutation : " + m + ", Crossover : " + c + ", Repair : " + r + " - Generation " + (g + 1) + " - Moyenne : " + moyenne + ", Ecart-type : " + ecartType);
//                        }
//                        moyennesMap.put("Selection : " + s + ", Mutation : " + m + ", Crossover : " + c + ", Repair : " + r, moyennes);
//                        ecartTypesMap.put("Selection : " + s + ", Mutation : " + m + ", Crossover : " + c + ", Repair : " + r, ecartTypes);
//
//                    }
//                }
//            }
//        }

        /******** POUR TESTER CERTAINS VARIANTS ********/
        // Variant 1
        List<List<Double>> result = new MainTest2().getMeanStdDev("tournament", "swap", "onepoint", "weight", ga);
        moyennesMap.put("Variant  1 :", result.get(0));
        ecartTypesMap.put("Variant  1 :", result.get(1));
        // Variant 2
        List<List<Double>> result2 = new MainTest2().getMeanStdDev("tournament", "mutation", "crossover", "repair", ga);
        moyennesMap.put("Variant  2 :", result2.get(0));
        ecartTypesMap.put("Variant  2 :", result2.get(1));
        // Variant 3
        List<List<Double>> result3 = new MainTest2().getMeanStdDev("selection", "swap", "multipoints", "weight", ga);
        moyennesMap.put("Variant  3 :", result3.get(0));
        ecartTypesMap.put("Variant  3 :", result3.get(1));
        // Variant 4
        List<List<Double>> result4 = new MainTest2().getMeanStdDev("selection", "mutation", "crossover", "repair", ga);
        moyennesMap.put("Variant  4 :", result4.get(0));
        ecartTypesMap.put("Variant  4 :", result4.get(1));

        // Display the performance chart
        SwingUtilities.invokeLater(() -> {
            PerformanceChart chart = new PerformanceChart("Courbe de performance", moyennesMap, ecartTypesMap);
            chart.setSize(1024, 768);
            chart.setLocationRelativeTo(null);
            chart.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            chart.setVisible(true);
        });

    }

    /**
     * Get the mean and standard deviation of the fitness values for a given variant
     * @param selection : selection method
     * @param mutation : mutation method
     * @param crossover : crossover method
     * @param repair : repair method
     * @param ga : genetic algorithm
     * @return Result.get(0) : List of means, Result.get(1) : List of standard deviations
     */
    private List<List<Double>> getMeanStdDev(String selection, String mutation, String crossover, String repair, GeneticAlgorithm ga) {
        List<List<Backpack>> backpacks = new ArrayList<>();
        for (int i = 0; i < NB_ITERATIONS; i++) {
            List<Backpack> solutions = ga.solveVariant(selection, mutation, crossover, repair, MUTATION_RATE, ELITIST_RATE);
            backpacks.add(solutions);
        }
        List<Double> moyennes = new ArrayList<>();
        List<Double> ecartTypes = new ArrayList<>();
        for (int g = 0; g < NB_GENERATIONS; g++) {
            int moyenne = 0;
            double ecartType = 0;
            for (int j = 0; j < NB_ITERATIONS; j++) {
                Backpack b = backpacks.get(j).get(g);
                moyenne += b.getFitness();
            }
            moyenne = moyenne / NB_ITERATIONS;
            moyennes.add((double) moyenne);
            for (int k = 0; k < NB_ITERATIONS; k++) {
                Backpack b = backpacks.get(k).get(g);
                ecartType += Math.pow((b.getFitness() - moyenne), 2);
            }
            ecartType = Math.sqrt(ecartType / NB_ITERATIONS);
            ecartTypes.add(ecartType);
        }
        List<List<Double>> result = new ArrayList<>();
        result.add(moyennes);
        result.add(ecartTypes);
        return result;
    }

}
