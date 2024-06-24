//package com.dauphine.juliejoelle;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Main {
//    final static int NB_ITEMS = 20;
//    final static int COSTS_SIZE = 5;
//    final static int POPULATION_SIZE = 100;
//    final static int NB_GENERATIONS = 20;
//    final static double MUTATION_RATE = 0.5;
//    final static double ELITIST_RATE = 0.1;
//    final static int NB_ITERATIONS = 30;
//
//    public static void main(String[] args) {
//        // Create items
//        List<Item> items = new ArrayList<>();
//        List<Integer> costs;
//        for (int i = 0; i < NB_ITEMS; i++) {
//            costs = new ArrayList<>();
//            int utility = (int) (Math.random() * 100);
//            for (int j = 0; j < COSTS_SIZE; j++) {
//                costs.add((int) (Math.random() * 15));
//            }
//            items.add(new Item(utility, costs));
//        }
//        // Add more items as needed
//
//        // Define budgets
//        List<Integer> budgets = new ArrayList<>();
//        for (int i = 0; i < COSTS_SIZE; i++) {
//            budgets.add((int) (Math.random() * 100)+25);
//        }
//
//        // Create backpack
//        Backpack backpack = new Backpack(budgets, items);
//
//        // Initialize and execute the genetic algorithm
//        GeneticAlgorithm ga = new GeneticAlgorithm(backpack, items, POPULATION_SIZE,NB_GENERATIONS);
//
//        // Solve the problem
////        List<List<Backpack>> backpacks = new ArrayList<>();
////        for (int i = 0; i < NB_ITERATIONS; i++) {
////            List<Backpack> solutions = ga.solve(MUTATION_RATE, ELITIST_RATE);
////            backpacks.add(solutions);
////        }
//
//        List<Integer> moyennes = new ArrayList<>();
//        List<Double> ecartTypes = new ArrayList<>();
//        for (int g = 0; g < NB_GENERATIONS; g++) {
//            int moyenne = 0;
//            double ecartType = 0;
//            for (int j = 0; j < NB_ITERATIONS; j++) {
//                Backpack b = backpacks.get(j).get(g);
//                moyenne += b.getFitness();
////                System.out.println("-------- Génération : " + g +" du tour : "+ j +" -------- ");
////                System.out.println("Best solution utility : "+b.getFitness());
////                System.out.print("Best solution costs : ");
////                for (int c = 0; c < budgets.size(); c++) {
////                    int cost = 0;
////                    for (int i = 0; i < b.getSolution().size(); i++) {
////                        if (b.getSolution().get(i)) {
////                            cost+=b.getObjects().get(i).getCosts().get(c);
////                        }
////                    }
////                    System.out.print(cost + " ");
////                }
////                System.out.println();
//            }
//            moyenne =moyenne/NB_ITERATIONS;
//            moyennes.add(moyenne);
//            for(int k = 0; k < NB_ITERATIONS; k++){
//                Backpack b = backpacks.get(k).get(g);
//                ecartType += Math.pow((b.getFitness()-moyenne),2);
//            }
//            ecartType =Math.sqrt(ecartType/NB_ITERATIONS);
//            ecartTypes.add(ecartType);
//            System.out.println("Generation " + (g + 1) + " - Moyenne : " + moyenne + ", Ecart-type : " + ecartType);
//        }
//
//    }
//
//}
