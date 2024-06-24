package com.dauphine.juliejoelle;

import java.util.*;

public class GeneticAlgorithm {
    private final Backpack backpack;
    private final List<Item> items;
    private List<Backpack> population;
    private final int populationSize;
    private final int nbGenerations;
    final static int TOURNAMENT_SIZE = 30;




    public GeneticAlgorithm(Backpack backpack, List<Item> items, int k, int nbGenerations) {
        this.items = items;
        this.population = new ArrayList<>();
        for (int i = 0; i < 2 * k; i++) {
            Backpack copy = new Backpack(backpack.getBudgets(), backpack.getObjects());
            do {
                copy.setSolution(this.generateRandomSolution(backpack.getObjects().size()));
            } while(!copy.isSolutionValid());
            this.population.add(copy);
        }
        this.backpack = backpack;
        this.populationSize = k*2;
        this.nbGenerations = nbGenerations;
    }

    private List<Boolean> generateRandomSolution(int size) {
        List<Boolean> solution = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            solution.add(Math.random() < 0.5);
        }
        return solution;
    }

    private ArrayList<Couple> selection() {
        ArrayList<Couple> couples = new ArrayList<>();
        Random random = new Random();
//        RandomSelector r = new RandomSelector();
//        for(int i = 0; i < populationSize; i++){
//            r.add(population.get(i).getFitness());
//        }
        for (int i = 1; i <= populationSize/2; i++) {
            int father = random.nextInt(populationSize);
            int mother = random.nextInt(populationSize);
            while(father == mother){
                mother = random.nextInt(populationSize);
            }
            Couple couple = new Couple(population.get(mother), population.get(father));
            couples.add(couple);
        }
        return couples;
    }

    private ArrayList<Couple> selectionByTournament(int tournamentSize) {
        ArrayList<Couple> couples = new ArrayList<>();
        for (int i = 0; i < populationSize / 2; i++) {
            Backpack parent1 = tournament(tournamentSize);
            Backpack parent2 = tournament(tournamentSize);
            while (parent1 == parent2) {
                parent2 = tournament(tournamentSize);
            }
            couples.add(new Couple(parent1, parent2));
        }
        return couples;
    }

    private Backpack tournament(int tournamentSize){
        List<Backpack> tournament = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < tournamentSize; i++) {
            int randomIndex = random.nextInt(population.size());
            tournament.add(population.get(randomIndex));
        }
        return tournament.stream().max(Comparator.comparingInt(Backpack::getFitness)).orElse(null);
    }

    private List<Backpack> crossover(List<Couple> parents) {
        List<Backpack> newPop = new ArrayList<>();
        RandomSelector r = new RandomSelector();
        r.add(1); r.add(1);
        for (Couple c: parents) {
            List<Boolean> newSolution1 = new ArrayList<>();
            List<Boolean> newSolution2 = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                int whichParent = r.randomChoice(); // Math.random()
                if (whichParent == 0) {
                    newSolution1.add(c.getFather().getSolution().get(i));
                    newSolution2.add(c.getMother().getSolution().get(i));
                }
                else {
                    newSolution1.add(c.getMother().getSolution().get(i));
                    newSolution2.add(c.getFather().getSolution().get(i));
                }
            }
            Backpack b1 = new Backpack(backpack.getBudgets(), backpack.getObjects());
            b1.setSolution(newSolution1);
            Backpack b2 = new Backpack(backpack.getBudgets(), backpack.getObjects());
            b2.setSolution(newSolution2);
            newPop.add(b1);
            newPop.add(b2);
        }
        return newPop;
    }

    private List<Backpack> onePointCrossover(List<Couple> parents) {
        List<Backpack> newPop = new ArrayList<>();
        Random random = new Random();
        int size = items.size();
        for (Couple c : parents) {
            List<Boolean> newSolution1 = new ArrayList<>();
            List<Boolean> newSolution2 = new ArrayList<>();
            int crossoverPoint = random.nextInt(size);
            for (int i = 0; i < size; i++) {
                if (i <= crossoverPoint) {
                    newSolution1.add(c.getFather().getSolution().get(i));
                    newSolution2.add(c.getMother().getSolution().get(i));
                } else {
                    newSolution1.add(c.getMother().getSolution().get(i));
                    newSolution2.add(c.getFather().getSolution().get(i));
                }
            }
            Backpack b1 = new Backpack(backpack.getBudgets(), backpack.getObjects());
            b1.setSolution(newSolution1);
            Backpack b2 = new Backpack(backpack.getBudgets(), backpack.getObjects());
            b2.setSolution(newSolution2);
            newPop.add(b1);
            newPop.add(b2);
        }
        return newPop;
    }

    private List<Backpack> multiPointCrossover(List<Couple> parents, int numPoints) {
        List<Backpack> newPop = new ArrayList<>();
        Random random = new Random();
        int size = items.size();

        for (Couple c : parents) {
            List<Boolean> newSolution1 = new ArrayList<>();
            List<Boolean> newSolution2 = new ArrayList<>();
            Set<Integer> crossoverPoints = new TreeSet<>();

            while (crossoverPoints.size() < numPoints) {
                crossoverPoints.add(random.nextInt(size));
            }

            boolean parentSwitch = false;
            for (int i = 0; i < size; i++) {
                if (crossoverPoints.contains(i)) {
                    parentSwitch = !parentSwitch;
                }
                if (parentSwitch) {
                    newSolution1.add(c.getMother().getSolution().get(i));
                    newSolution2.add(c.getFather().getSolution().get(i));
                } else {
                    newSolution1.add(c.getFather().getSolution().get(i));
                    newSolution2.add(c.getMother().getSolution().get(i));
                }
            }

            Backpack b1 = new Backpack(backpack.getBudgets(), backpack.getObjects());
            b1.setSolution(newSolution1);
            Backpack b2 = new Backpack(backpack.getBudgets(), backpack.getObjects());
            b2.setSolution(newSolution2);
            newPop.add(b1);
            newPop.add(b2);
        }
        return newPop;
    }

    private Backpack mutation(Backpack backpack) {
        List<Boolean> newSolution = new ArrayList<>(backpack.getSolution());
        Random random = new Random();
        int mutationPoint = random.nextInt(newSolution.size());
        newSolution.set(mutationPoint, !newSolution.get(mutationPoint));
        backpack.setSolution(newSolution);
        return backpack;
    }

    private Backpack mutationSwap(Backpack backpack) {
        List<Boolean> newSolution = new ArrayList<>(backpack.getSolution());
        Random random = new Random();
        int mutationPoint1 = random.nextInt(newSolution.size());
        int mutationPoint2 = mutationPoint1;
        if (newSolution.get(mutationPoint1)) {
            newSolution.set(mutationPoint1, false);
            while (mutationPoint2 == mutationPoint1 && !newSolution.get(mutationPoint2)) {
                mutationPoint2 = random.nextInt(newSolution.size());
            }
            newSolution.set(mutationPoint2, true);
        } else {
            newSolution.set(mutationPoint1, true);
            while (mutationPoint2 == mutationPoint1 && newSolution.get(mutationPoint2)) {
                mutationPoint2 = random.nextInt(newSolution.size());
            }
            newSolution.set(mutationPoint2, false);
        }
        backpack.setSolution(newSolution);
        return backpack;
    }

//    public List<Backpack> solve(double mutationRate, double elitistRate) {
//        List<Backpack> newPopulation = new ArrayList<>(population);
//        List<Backpack> solutions = new LinkedList<>();
//        for(int g = 0; g < this.nbGenerations; g++){
//            List<Couple> parents = selection();
//            newPopulation = crossover(parents);
//            for (int i = 0; i < newPopulation.size(); i++) {
//                if (Math.random() < mutationRate) {
//                    newPopulation.set(i, mutation(newPopulation.get(i)));
//                }
//                newPopulation.get(i).repair();
//            }
//            for(int j = 1; j <= populationSize * elitistRate; j++){
//                this.replace(newPopulation);
//            }
//
//            int numberOfElitist = (int) (populationSize * elitistRate);
//            List<Backpack> worstBackapcks = newPopulation.stream().sorted(Comparator.comparing(Backpack::getFitness)).limit(numberOfElitist).toList();
//            List<Backpack> bestBackapcks = population.stream().sorted(Comparator.comparing(Backpack::getFitness, Collections.reverseOrder())).limit(numberOfElitist).toList();
//
//            for (int i = 0; i < worstBackapcks.size(); i++) {
//                newPopulation.remove(worstBackapcks.get(i));
//                newPopulation.add(bestBackapcks.get(i));
//            }
//            population = newPopulation;
//
//            solutions.add(getBest(newPopulation));
//        }
//        return solutions;
//    }

    private Backpack getBest(List<Backpack> population) {
        return population.stream().max(Comparator.comparingInt(Backpack::getFitness)).orElse(null);
    }

    private Backpack getWorst(List<Backpack> population) {
        return population.stream().min(Comparator.comparingInt(Backpack::getFitness)).orElse(null);
    }

    private void replace(List<Backpack> newPopulation) {
        Backpack worst = getWorst(newPopulation);
        Backpack best = getBest(this.population);
        if (worst != null && best != null && !newPopulation.contains(best)) {
            newPopulation.remove(worst);
            newPopulation.add(best);
            System.out.println("Replacing worst solution with best solution "+ worst.getFitness() + " with " + best.getFitness());
        }
    }

    public List<Backpack> solveVariant(String s, String m, String c, String r, double mutationRate, double elitistRate) {
//        System.out.println(getBest(population).getFitness());
        List<Backpack> newPopulation;
        List<Backpack> solutions = new LinkedList<>();
        for(int g = 0; g < this.nbGenerations; g++){
            List<Couple> parents = switch (s) {
                case "selection" -> selection();
                case "tournament" -> selectionByTournament(TOURNAMENT_SIZE);
                default -> selection();
            };
            newPopulation = switch (c) {
                case "crossover" -> crossover(parents);
                case "onepoint" -> onePointCrossover(parents);
                case "multipoints" -> multiPointCrossover(parents, 2);
                default -> crossover(parents);
            };

            for (int i = 0; i < newPopulation.size(); i++) {
                if (Math.random() < mutationRate) {
                    switch (m) {
                        case "mutation":
                            newPopulation.set(i, mutation(newPopulation.get(i)));
                            break;
                        case "swap":
                            newPopulation.set(i, mutationSwap(newPopulation.get(i)));
                            break;
                        default:
                            newPopulation.set(i, mutation(newPopulation.get(i)));
                            break;
                    }
                }
                switch (r) {
                    case "repair":
                        newPopulation.get(i).repair();
                        break;
                    case "weight":
                        newPopulation.get(i).repairByWeight();
                        break;
                    default:
                        newPopulation.get(i).repair();
                        break;
                }
            }
            for(int j = 1; j <= populationSize * elitistRate; j++){
                this.replace(newPopulation);
            }
            population = newPopulation;
//            System.out.println(population);
            System.out.println(getBest(population).getFitness());
            solutions.add(getBest(newPopulation));
        }
        return solutions;
    }
}
