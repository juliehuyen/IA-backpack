package com.dauphine.juliejoelle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainTest {
    final static int NB_ITEMS = 6; // Number of items in the first problem
    final static int COSTS_SIZE = 10; // Number of constraints in the first problem
    final static int POPULATION_SIZE = 100;
    final static int NB_GENERATIONS = 20;
    final static double MUTATION_RATE = 0.5;
    final static double ELITIST_RATE = 0.1;
    final static int NB_ITERATIONS = 3;

    public static void main(String[] args) {
        // Create items
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

        // Solve the problem
//        List<List<Backpack>> backpacks = new ArrayList<>();
//        for (int i = 0; i < NB_ITERATIONS; i++) {
//            List<Backpack> solutions = ga.solve(MUTATION_RATE, ELITIST_RATE);
//            backpacks.add(solutions);
//        }


//        for (int g = 0; g < NB_GENERATIONS; g++) {
//            int moyenne = 0;
//            double ecartType = 0;
//            for (int j = 0; j < NB_ITERATIONS; j++) {
//                Backpack b = backpacks.get(j).get(g);
//                moyenne += b.getFitness();
//            }
//            moyenne = moyenne / NB_ITERATIONS;
//            for (int k = 0; k < NB_ITERATIONS; k++) {
//                Backpack b = backpacks.get(k).get(g);
//                ecartType += Math.pow((b.getFitness() - moyenne), 2);
//            }
//            ecartType = Math.sqrt(ecartType / NB_ITERATIONS);
//            System.out.println("Generation " + (g + 1) + " - Moyenne : " + moyenne + ", Ecart-type : " + ecartType);
//        }
    }

    private static List<Integer> getCostsForItem(List<List<Integer>> costsList, int itemIndex) {
        List<Integer> costsForItem = new ArrayList<>();
        for (List<Integer> costs : costsList) {
            costsForItem.add(costs.get(itemIndex));
        }
        return costsForItem;
    }
}
