package com.dauphine.juliejoelle.algorithm;

import com.dauphine.juliejoelle.RandomSelector;

import java.util.*;

public class GeneticAlgorithm {
    private final Backpack backpack;
    private final List<Item> items;
    private List<Backpack> population;
    private final int populationSize;
    private final int nbGenerations;
    final static int TOURNAMENT_SIZE = 15;

    public GeneticAlgorithm(Backpack backpack, List<Item> items, int k, int nbGenerations) {
        this.items = items;
        this.population = new ArrayList<>();
        System.out.println("Creating initial population...");
        for (int i = 0; i < 2 * k; i++) {
            Backpack copy = new Backpack(backpack.getBudgets(), backpack.getObjects());
            do {
                System.out.println("Generating random solution... " + i );
                copy.setSolution(this.generateRandomSolution(backpack.getObjects().size()));
            } while(!copy.isSolutionValid());
            System.out.println("Adding backpack to population");
            this.population.add(copy);
        }
        this.backpack = backpack;
        this.populationSize = k * 2;
        this.nbGenerations = nbGenerations;
    }

    /**
     * Generates a random solution for the backpack problem
     * @param size the size of the solution
     * @return a list of booleans representing the solution
     */
    private List<Boolean> generateRandomSolution(int size) {
        List<Boolean> solution = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            solution.add(Math.random() < 0.5);
        }
        return solution;
    }

    /**
     * Selects the parents for the crossover operation
     * @return a list of couples of backpacks
     */
    private ArrayList<Couple> selection() {
        ArrayList<Couple> couples = new ArrayList<>();
        Random random = new Random();
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

    /**
     * Selects the parents for the crossover operation using a tournament
     * @param tournamentSize the size of the tournament
     * @return a list of couples of backpacks
     */
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

    /**
     * Selects the best backpack in a tournament
     * @param tournamentSize the size of the tournament
     * @return the best backpack
     */
    private Backpack tournament(int tournamentSize){
        List<Backpack> tournament = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < tournamentSize; i++) {
            int randomIndex = random.nextInt(population.size());
            tournament.add(population.get(randomIndex));
        }
        return tournament.stream().max(Comparator.comparingInt(Backpack::getFitness)).orElse(null);
    }

    /**
     * Performs the standard crossover operation
     * @param parents the parents
     * @return a list of backpacks
     */
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

    /**
     * Performs the one-point crossover operation
     * @param parents the parents
     * @return a list of backpacks
     */
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

    /**
     * Performs the multi-points crossover operation
     * @param parents the parents
     * @param numPoints the number of crossover points
     * @return a list of backpacks
     */
    private List<Backpack> multiPointsCrossover(List<Couple> parents, int numPoints) {
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

    /**
     * Performs the standard mutation operation
     * @param backpack the backpack to mutate
     * @return the mutated backpack
     */
    private Backpack mutation(Backpack backpack) {
        List<Boolean> newSolution = new ArrayList<>(backpack.getSolution());
        Random random = new Random();
        int mutationPoint = random.nextInt(newSolution.size());
        newSolution.set(mutationPoint, !newSolution.get(mutationPoint));
        backpack.setSolution(newSolution);
        return backpack;
    }

    /**
     * Performs the swap mutation operation
     * @param backpack the backpack to mutate
     * @return the mutated backpack
     */
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

    /**
     * Returns the best backpack in a population
     * @param population the population
     * @return the best backpack
     */
    private Backpack getBest(List<Backpack> population) {
        return population.stream().max(Comparator.comparingInt(Backpack::getFitness)).orElse(null);
    }

    /**
     * Returns the worst backpack in a population
     * @param population the population
     * @return the worst backpack
     */
    private Backpack getWorst(List<Backpack> population) {
        return population.stream().min(Comparator.comparingInt(Backpack::getFitness)).orElse(null);
    }

    /**
     * Replaces the worst backpack in the new population with the best backpack in current population
     * @param newPopulation the new population
     */
    private void replace(List<Backpack> newPopulation) {
        Backpack worst = getWorst(newPopulation);
        Backpack best = getBest(this.population);
        if (worst != null && best != null && !newPopulation.contains(best)) {
            newPopulation.remove(worst);
            newPopulation.add(best);
        }
    }

    /**
     * Solves the backpack problem using a genetic algorithm
     * @param s the selection method
     * @param m the mutation method
     * @param c the crossover method
     * @param r the repair method
     * @param mutationRate the mutation rate
     * @param elitistRate the elitist rate
     * @return a list of backpacks representing the best solution at each generation
     */
    public List<Backpack> solveVariant(String s, String m, String c, String r, double mutationRate, double elitistRate) {
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
                case "multipoints" -> multiPointsCrossover(parents, 2);
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
            solutions.add(getBest(newPopulation));
        }
        return solutions;
    }
}
