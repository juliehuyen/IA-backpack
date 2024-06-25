package com.dauphine.juliejoelle.main3;

import com.dauphine.juliejoelle.algorithm.Backpack;
import com.dauphine.juliejoelle.algorithm.GeneticAlgorithm;
import com.dauphine.juliejoelle.algorithm.Item;
import com.dauphine.juliejoelle.display.PerformanceChart;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main3B {
    final static int NB_ITEMS = 50;
    final static int COSTS_SIZE = 5;
    final static int POPULATION_SIZE = 100;
    final static int NB_GENERATIONS = 50;
    final static double MUTATION_RATE = 0.2;
    final static double ELITIST_RATE = 0.1;
    final static int NB_ITERATIONS = 100;

    public static void main(String[] args) {
        Long start = System.currentTimeMillis();
        // Create items
        List<Item> items = new ArrayList<>();
        List<Integer> utilities = Arrays.asList(
                560, 1125, 300, 620, 2100, 431, 68, 328, 47, 122,
                322, 196, 41, 25, 425, 4260, 416, 115, 82, 22,
                631, 132, 420, 86, 42, 103, 215, 81, 91, 26,
                49, 420, 316, 72, 71, 49, 108, 116, 90, 738,
                1811, 430, 3060, 215, 58, 296, 620, 418, 47, 81
        );

        List<List<Integer>> costsList = Arrays.asList(
                Arrays.asList(40, 16, 38, 8, 38), Arrays.asList(91, 92, 39, 71, 52), Arrays.asList(10, 41, 32, 30, 30), Arrays.asList(30, 16, 71, 60, 42), Arrays.asList(160, 150, 80, 200, 170),
                Arrays.asList(20, 23, 26, 18, 9), Arrays.asList(3, 4, 5, 6, 7), Arrays.asList(12, 18, 40, 30, 20), Arrays.asList(3, 6, 8, 4, 0), Arrays.asList(18, 0, 12, 8, 3),
                Arrays.asList(9, 12, 30, 31, 21), Arrays.asList(25, 8, 15, 6, 4), Arrays.asList(1, 2, 0, 3, 1), Arrays.asList(1, 1, 1, 0, 2), Arrays.asList(10, 0, 23, 18, 14),
                Arrays.asList(280, 200, 100, 60, 310), Arrays.asList(10, 20, 0, 21, 8), Arrays.asList(8, 6, 20, 4, 4), Arrays.asList(1, 2, 3, 0, 6), Arrays.asList(1, 1, 0, 2, 1),
                Arrays.asList(49, 70, 40, 32, 18), Arrays.asList(8, 9, 6, 15, 15), Arrays.asList(21, 22, 8, 31, 38), Arrays.asList(6, 4, 2, 2, 10), Arrays.asList(1, 1, 1, 7, 4),
                Arrays.asList(5, 5, 5, 8, 0), Arrays.asList(10, 10, 10, 2, 10), Arrays.asList(8, 6, 6, 8, 6), Arrays.asList(2, 4, 4, 8, 1), Arrays.asList(1, 0, 0, 3, 5),
                Arrays.asList(0, 10, 6, 20, 4), Arrays.asList(10, 0, 12, 8, 30), Arrays.asList(42, 10, 28, 31, 12), Arrays.asList(6, 8, 6, 6, 16), Arrays.asList(4, 6, 28, 3, 18),
                Arrays.asList(8, 12, 93, 6, 22), Arrays.asList(0, 0, 9, 0, 14), Arrays.asList(10, 10, 30, 6, 20), Arrays.asList(1, 1, 22, 2, 6), Arrays.asList(40, 40, 0, 1, 30),
                Arrays.asList(86, 6, 36, 0, 12), Arrays.asList(11, 12, 45, 20, 16), Arrays.asList(120, 120, 13, 14, 18), Arrays.asList(8, 8, 2, 20, 3), Arrays.asList(3, 3, 2, 0, 1),
                Arrays.asList(32, 32, 6, 8, 2), Arrays.asList(28, 28, 1, 1, 0), Arrays.asList(13, 13, 4, 2, 3), Arrays.asList(2, 2, 1, 1, 0), Arrays.asList(4, 4, 0, 0, 1)
        );

        for (int i = 0; i < NB_ITEMS; i++) {
            items.add(new Item(utilities.get(i), costsList.get(i)));
        }

        // Define budgets
        List<Integer> budgets = Arrays.asList(800, 650, 550, 550, 650);

        // Create backpack
        Backpack backpack = new Backpack(budgets, items);

        // Initialize and execute the genetic algorithm
        GeneticAlgorithm ga;

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

        /******** POUR TESTER LES VARIANTS SELECTIONNES ********/
        // Variant 1
        ga = new GeneticAlgorithm(backpack, items, POPULATION_SIZE, NB_GENERATIONS);
        Long startVariant1 = System.currentTimeMillis();
        System.out.println("Début variant 1 :");
        List<List<Double>> result = new Main3B().getMeanStdDev("selection", "mutation", "onepoint", "weight", ga);
        moyennesMap.put("Variant  1 :", result.get(0));
        ecartTypesMap.put("Variant  1 :", result.get(1));
        System.out.println("Fin variant 1 :");
        Long endVariant1 = System.currentTimeMillis();
        // Variant 2
        ga = new GeneticAlgorithm(backpack, items, POPULATION_SIZE, NB_GENERATIONS);
        Long startVariant2 = System.currentTimeMillis();
        System.out.println("Début variant 2 :");
        List<List<Double>> result2 = new Main3B().getMeanStdDev("selection", "swap", "crossover", "repair", ga);
        moyennesMap.put("Variant  2 :", result2.get(0));
        ecartTypesMap.put("Variant  2 :", result2.get(1));
        System.out.println("Fin variant 2 :");
        Long endVariant2 = System.currentTimeMillis();
        // Variant 3
        ga = new GeneticAlgorithm(backpack, items, POPULATION_SIZE, NB_GENERATIONS);
        Long startVariant3 = System.currentTimeMillis();
        System.out.println("Début variant 3 :");
        List<List<Double>> result3 = new Main3B().getMeanStdDev("selection", "swap", "multipoints", "weight", ga);
        moyennesMap.put("Variant  3 :", result3.get(0));
        ecartTypesMap.put("Variant  3 :", result3.get(1));
        System.out.println("Fin variant 3 :");
        Long endVariant3 = System.currentTimeMillis();
        // Variant 4
        ga = new GeneticAlgorithm(backpack, items, POPULATION_SIZE, NB_GENERATIONS);
        Long startVariant4 = System.currentTimeMillis();
        System.out.println("Début variant 4 :");
        List<List<Double>> result4 = new Main3B().getMeanStdDev("tournament", "mutation", "multipoints", "repair", ga);
        moyennesMap.put("Variant  4 :", result4.get(0));
        ecartTypesMap.put("Variant  4 :", result4.get(1));
        System.out.println("Fin variant 4 :");
        Long endVariant4 = System.currentTimeMillis();

        // Display the performance chart
        SwingUtilities.invokeLater(() -> {
            PerformanceChart chart = new PerformanceChart("Courbe de performance", moyennesMap, ecartTypesMap);
            chart.setSize(1024, 768);
            chart.setLocationRelativeTo(null);
            chart.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            chart.setVisible(true);
        });
        Long end = System.currentTimeMillis();
        System.out.println("Total execution time : " + (end - start) + " ms");
        System.out.println("Variant 1 execution time : " + (endVariant1 - startVariant1) + " ms");
        System.out.println("Variant 2 execution time : " + (endVariant2 - startVariant2) + " ms");
        System.out.println("Variant 3 execution time : " + (endVariant3 - startVariant3) + " ms");
        System.out.println("Variant 4 execution time : " + (endVariant4 - startVariant4) + " ms");
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
            System.out.println("itération : " + i);
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
