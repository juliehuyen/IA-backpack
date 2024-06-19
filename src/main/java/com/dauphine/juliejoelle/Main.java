package com.dauphine.juliejoelle;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Create items
        int nbItems = 20;
        int sizeCosts = 5;
        List<Item> items = new ArrayList<>();
        List<Integer> costs = new ArrayList<>();
        for(int i = 0; i < nbItems; i++) {
            costs = new ArrayList<>();
            int utility = (int) (Math.random() * 100);
            for(int j = 0; j < sizeCosts; j++) {
                costs.add((int) (Math.random() * 15));
            }
            items.add(new Item(utility, costs));
        }
        // Add more items as needed

        // Define budgets
        List<Integer> budgets = new ArrayList<>();
        for(int i = 0; i < costs.size(); i++) {
            budgets.add((int) (Math.random() * 100)+25);
        }

        // Create backpack
        Backpack backpack = new Backpack(budgets, items);

        // Initialize and execute the genetic algorithm
        int populationSize = 100; // Example size
        int nbGenerations = 20;
        GeneticAlgorithm ga = new GeneticAlgorithm(backpack, items, populationSize,nbGenerations);

        // Parameters for the genetic algorithm
        double mutationRate = 0.05;
        double elitistRate = 0.1;
        int tour = 3;

        // Solve the problem
        List<List<Backpack>> backpacks = new ArrayList<>();
        for(int i = 0; i < tour; i++) {
            List<Backpack> solutions = ga.solve(mutationRate, elitistRate);
            backpacks.add(solutions);
        }

        List<List<Integer>> generationFitness = new ArrayList<>();
        for (int i = 0; i < NB_GENERATIONS; i++) {
            generationFitness.add(new ArrayList<>());
        }

        // Solve the problem multiple times and collect fitness values
        for (int i = 0; i < NB_ITERATIONS; i++) {
            List<Backpack> solutions = ga.solve(MUTATION_RATE, ELITIST_RATE);
            for (int gen = 0; gen < NB_GENERATIONS; gen++) {
                int bestFitness = solutions.get(gen).getFitness();
                generationFitness.get(gen).add(bestFitness);
            }
        }

        // Calculate and print the mean and standard deviation for each generation
        for (int gen = 0; gen < NB_GENERATIONS; gen++) {
            double mean = ga.calculateMean(generationFitness.get(gen));
            double stddev = ga.calculateStandardDeviation(generationFitness.get(gen));
//            System.out.println("Generation " + (gen + 1) + " - Mean Fitness: " + mean + ", Standard Deviation: " + stddev);
        }

        for (int g = 0; g < NB_GENERATIONS; g++) {
            int moyenne = 0;
            double ecartType = 0;
            for (int j = 0; j < NB_ITERATIONS; j++) {
                Backpack b = backpacks.get(j).get(g);
                moyenne += b.getFitness();
//                System.out.println("-------- Génération : " + g +" du tour : "+ j +" -------- ");
//                System.out.println("Best solution utility : "+b.getFitness());
//                System.out.print("Best solution costs : ");
//                for (int c = 0; c < budgets.size(); c++) {
//                    int cost = 0;
//                    for (int i = 0; i < b.getSolution().size(); i++) {
//                        if (b.getSolution().get(i)) {
//                            cost+=b.getObjects().get(i).getCosts().get(c);
//                        }
//                    }
//                    System.out.print(cost + " ");
//                }
//                System.out.println();
            }
            moyenne =moyenne/NB_ITERATIONS;
            for(int k = 0; k < NB_ITERATIONS; k++){
                Backpack b = backpacks.get(k).get(g);
                ecartType += Math.pow((b.getFitness()-moyenne),2);
            }
            ecartType =Math.sqrt(ecartType/NB_ITERATIONS);
            System.out.println("Generation " + (g + 1) + " - Moyenne : " + moyenne + ", Ecart-type : " + ecartType);
        }



        // Print the best solution
//        System.out.println("Best solution utility: " + bestSolution.getFitness());
//        System.out.println("Best solution costs: ");
//        for (int j = 0; j < budgets.size(); j++) {
//            int totalCost = 0;
//            for (int i = 0; i < bestSolution.getSolution().size(); i++) {
//                if (bestSolution.getSolution().get(i)) {
//                    totalCost += bestSolution.getObjects().get(i).getCosts().get(j);
//                }
//            }
//            System.out.print(totalCost + " ");
//        }
//        System.out.println();
    }
}
