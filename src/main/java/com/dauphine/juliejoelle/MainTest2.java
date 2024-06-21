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
        List<Integer> utilities = Arrays.asList(100, 600, 1200, 2400, 500, 2000);
        List<List<Integer>> costsList = Arrays.asList(
                Arrays.asList(8,8,3,5,5,5,0,3,3,3),
                Arrays.asList(12,12,6,10,13,13,0,0,2,2),
                Arrays.asList(13,13,4,8,8,8,0,4,4,4),
                Arrays.asList(64,75,18,32,42,48,0,8,8,8),
                Arrays.asList(22,22,6,6,6,6,8,0,0,8),
                Arrays.asList(41,41,4,12,20,20,0,0,4,4)
        );

        for (int i = 0; i < NB_ITEMS; i++) {
            items.add(new Item(utilities.get(i), costsList.get(i)));
        }

        // Define budgets
        List<Integer> budgets = Arrays.asList(80, 96, 20, 36, 44, 48, 10, 18, 22, 24);

        // Create backpack
        Backpack backpack = new Backpack(budgets, items);

        // Initialize and execute the genetic algorithm
        GeneticAlgorithm ga = new GeneticAlgorithm(backpack, items, POPULATION_SIZE, NB_GENERATIONS);

        HashMap<String, List<Double>> moyennesMap = new HashMap<>();
        HashMap<String, List<Double>> ecartTypesMap = new HashMap<>();

        // Solve the problem
        List<String> selections = Arrays.asList("selection", "tournament");
        List<String> mutations = Arrays.asList("mutation", "swap");
        List<String> crossovers = Arrays.asList("crossover", "onepoint");
        List<String> repairs = Arrays.asList("repair", "weight");
        for (String s : selections) {
            for (String m : mutations) {
                for (String c : crossovers) {
                    for (String r : repairs) {
                        List<List<Backpack>> backpacks = new ArrayList<>();
                        for (int i = 0; i < NB_ITERATIONS; i++) {
                            List<Backpack> solutions = ga.solveVariant(s, m, c, r, MUTATION_RATE, ELITIST_RATE);
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
                            System.out.println("Selection : " + s + ", Mutation : " + m + ", Crossover : " + c + ", Repair : " + r + " - Generation " + (g + 1) + " - Moyenne : " + moyenne + ", Ecart-type : " + ecartType);
                        }
                        moyennesMap.put("Selection : " + s + ", Mutation : " + m + ", Crossover : " + c + ", Repair : " + r, moyennes);
                        ecartTypesMap.put("Selection : " + s + ", Mutation : " + m + ", Crossover : " + c + ", Repair : " + r, ecartTypes);

                    }
                }
            }
        }



        SwingUtilities.invokeLater(() -> {
            PerformanceChart chart = new PerformanceChart("Courbe de performance", moyennesMap, ecartTypesMap);
            chart.setSize(1920, 1080);
            chart.setLocationRelativeTo(null);
            chart.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            chart.setVisible(true);
        });

    }

}
