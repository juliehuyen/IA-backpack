package com.dauphine.juliejoelle;

import java.util.ArrayList;
import java.util.Arrays;
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
//        items.add(new Item(10, Arrays.asList(5, 3)));
//        items.add(new Item(6, Arrays.asList(4, 2)));
//        items.add(new Item(8, Arrays.asList(6, 1)));
//        items.add(new Item(7, Arrays.asList(3, 5)));
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
        for(int i = 0; i < nbGenerations; i++) {
            for(int j = 0; j < tour; j++) {
                System.out.println("-------- Génération : " + i +" du tour : "+ j +" -------- ");
                System.out.println("Best solution utility : "+backpacks.get(j).get(i).getFitness());
                System.out.print("Best solution costs : ");
                for(int c = 0; c < budgets.size(); c++) {
                    int cost = 0;
                    for(int s = 0; s < backpacks.get(j).get(i).getSolution().size(); s++) {
                        if(backpacks.get(j).get(i).getSolution().get(s)){
                            cost+=backpacks.get(j).get(i).getObjects().get(s).getCosts().get(c);
                        }
                    }
                    System.out.print(cost + " ");
                }
                System.out.println();
            }

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
