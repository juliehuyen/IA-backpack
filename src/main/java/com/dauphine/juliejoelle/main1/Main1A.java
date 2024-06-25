package com.dauphine.juliejoelle.main1;

import com.dauphine.juliejoelle.algorithm.Backpack;
import com.dauphine.juliejoelle.algorithm.GeneticAlgorithm;
import com.dauphine.juliejoelle.algorithm.Item;
import com.dauphine.juliejoelle.display.PerformanceChart;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Jeu de données 1 (aléatoire)
 * Choix de paramètres 1
 */
public class Main1A {
    final static int NB_ITEMS = 40;
    final static int COSTS_SIZE = 8;
    final static int POPULATION_SIZE = 10;
    final static int NB_GENERATIONS = 20;
    final static double MUTATION_RATE = 0.4;
    final static double ELITIST_RATE = 0.1;
    final static int NB_ITERATIONS = 100;

    public static void main(String[] args) {
        Long start = System.currentTimeMillis();
        // Create items
        List<Item> items = new ArrayList<>();
        List<Integer> costs;
        System.out.println("Creating items...");
        for (int i = 0; i < NB_ITEMS; i++) {
            costs = new ArrayList<>();
            int utility = (int) (Math.random() * 100);
            for (int j = 0; j < COSTS_SIZE; j++) {
                costs.add((int) (Math.random() * 15));
            }
            items.add(new Item(utility, costs));
        }

        // Define budgets
        System.out.println("Defining budgets...");
        List<Integer> budgets = new ArrayList<>();
        for (int i = 0; i < COSTS_SIZE; i++) {
            budgets.add((int) (Math.random() * 100)+25);
        }

        // Create backpack
        Backpack backpack = new Backpack(budgets, items);

        // Initialize and execute the genetic algorithm
        GeneticAlgorithm ga;

        HashMap<String, List<Double>> moyennesMap = new HashMap<>();
        HashMap<String, List<Double>> ecartTypesMap = new HashMap<>();

        /******** POUR TESTER TOUS LES VARIANTS ********/
        // Solve the problem
//        List<String> selections = Arrays.asList("selection", "tournament");
//        List<String> mutations = Arrays.asList("mutation", "swap");
//        List<String> crossovers = Arrays.asList("crossover", "onepoint","multipoints");
//        List<String> repairs = Arrays.asList("repair", "weight");

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
        /**********************************/

        /******** POUR TESTER LES VARIANTS SELECTIONNES ********/
        // Variant 1
        ga = new GeneticAlgorithm(backpack, items, POPULATION_SIZE, NB_GENERATIONS);
        Long startVariant1 = System.currentTimeMillis();
        System.out.println("Début variant 1 :");
        List<List<Double>> result = new Main1A().getMeanStdDev("selection", "mutation", "onepoint", "weight", ga);
        moyennesMap.put("Variant  1 :", result.get(0));
        ecartTypesMap.put("Variant  1 :", result.get(1));
        System.out.println("Fin variant 1 :");
        Long endVariant1 = System.currentTimeMillis();
        // Variant 2
        ga = new GeneticAlgorithm(backpack, items, POPULATION_SIZE, NB_GENERATIONS);
        Long startVariant2 = System.currentTimeMillis();
        System.out.println("Début variant 2 :");
        List<List<Double>> result2 = new Main1A().getMeanStdDev("selection", "swap", "crossover", "repair", ga);
        moyennesMap.put("Variant  2 :", result2.get(0));
        ecartTypesMap.put("Variant  2 :", result2.get(1));
        System.out.println("Fin variant 2 :");
        Long endVariant2 = System.currentTimeMillis();
        // Variant 3
        ga = new GeneticAlgorithm(backpack, items, POPULATION_SIZE, NB_GENERATIONS);
        Long startVariant3 = System.currentTimeMillis();
        System.out.println("Début variant 3 :");
        List<List<Double>> result3 = new Main1A().getMeanStdDev("selection", "swap", "multipoints", "weight", ga);
        moyennesMap.put("Variant  3 :", result3.get(0));
        ecartTypesMap.put("Variant  3 :", result3.get(1));
        System.out.println("Fin variant 3 :");
        Long endVariant3 = System.currentTimeMillis();
        // Variant 4
        ga = new GeneticAlgorithm(backpack, items, POPULATION_SIZE, NB_GENERATIONS);
        Long startVariant4 = System.currentTimeMillis();
        System.out.println("Début variant 4 :");
        List<List<Double>> result4 = new Main1A().getMeanStdDev("tournament", "mutation", "multipoints", "repair", ga);
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
