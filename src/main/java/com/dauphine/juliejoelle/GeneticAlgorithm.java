package com.dauphine.juliejoelle;

import java.util.*;

public class GeneticAlgorithm {
    private Backpack backpack;
    private List<Item> items;
    private List<Backpack> population;
    private int populationSize;
    private int nbGenerations;
    final static int TOURNAMENT_SIZE = 30;


    public GeneticAlgorithm(Backpack backpack, List<Item> items, int k, int nbGenerations) {
        this.items = items;
        this.population = new ArrayList<>();
        for (int i = 0; i < 2 * k; i++) {
            Backpack copy = new Backpack(backpack.getBudgets(), backpack.getObjects());
            copy.setSolution(this.generateRandomSolution(backpack.getObjects().size()));
            this.population.add(copy);
        }
        this.backpack = backpack;
        this.populationSize = k*2;
        this.nbGenerations = nbGenerations;
    }

    private List<Boolean> generateRandomSolution(int size) {
        List<Boolean> solution = new ArrayList<Boolean>();
        for (int i = 0; i < size; i++) {
            solution.add(Math.random() < 0.5);
        }
        return solution;
    }

    // Variant possible
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

    private Backpack tournament(int tournamentSize) {
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
        if (newSolution.get(mutationPoint1)) {
            newSolution.set(mutationPoint1, false);
        } else {
            newSolution.set(mutationPoint1, true);
        }
        int mutationPoint2 = mutationPoint1;
        while (mutationPoint2 == mutationPoint1) {
            mutationPoint2 = random.nextInt(newSolution.size());
        }
        if (newSolution.get(mutationPoint2)) {
            newSolution.set(mutationPoint2, false);
        } else {
            newSolution.set(mutationPoint2, true);
        }
        backpack.setSolution(newSolution);
        return backpack;
    }

    public List<Backpack> solve(double mutationRate, double elitistRate) {
        List<Backpack> newPopulation = new ArrayList<>(population);
        List<Backpack> solutions = new LinkedList<>();
        for(int g = 0; g < this.nbGenerations; g++){
            List<Couple> parents = selection();
            newPopulation = crossover(parents);
            for (int i = 0; i < newPopulation.size(); i++) {
                if (Math.random() < mutationRate) {
                    newPopulation.set(i, mutation(newPopulation.get(i)));
                }
                newPopulation.get(i).repair();
            }
            for(int j = 1; j <= populationSize * elitistRate; j++){
                this.replace(newPopulation);
            }
            population = newPopulation;

            solutions.add(getBest(newPopulation));
        }
        return solutions;
    }

    public List<Backpack> solveVariantSelectionByTournament(double mutationRate, double elitistRate) {
        List<Backpack> newPopulation = new ArrayList<>(population);
        List<Backpack> solutions = new LinkedList<>();
        for(int g = 0; g < this.nbGenerations; g++){
            boolean test = false;
            while (!test) {
                List<Couple> parents = selectionByTournament(TOURNAMENT_SIZE);
                newPopulation = crossover(parents);
                for (int i = 0; i < newPopulation.size(); i++) {
                    if (Math.random() < mutationRate) {
                        newPopulation.set(i, mutation(newPopulation.get(i)));
                    }
                    newPopulation.get(i).repair();
                }
                for(int j = 1; j <= populationSize * elitistRate; j++){
                    this.replace(newPopulation);
                }
                population = newPopulation;
                test = (getBest(population).isSolutionValid());
            }
            solutions.add(getBest(newPopulation));
        }
        return solutions;
    }

    public List<Backpack> solveVariantOnePointCrossover(double mutationRate, double elitistRate) {
        List<Backpack> newPopulation = new ArrayList<>(population);
        List<Backpack> solutions = new LinkedList<>();
        for(int g = 0; g < this.nbGenerations; g++){
            boolean test = false;
            while (!test) {
                List<Couple> parents = selection();
                newPopulation = onePointCrossover(parents);
                for (int i = 0; i < newPopulation.size(); i++) {
                    if (Math.random() < mutationRate) {
                        newPopulation.set(i, mutation(newPopulation.get(i)));
                    }
                    newPopulation.get(i).repair();
                }
                for(int j = 1; j <= populationSize * elitistRate; j++){
                    this.replace(newPopulation);
                }
                population = newPopulation;
                test = (getBest(population).isSolutionValid());
            }
            solutions.add(getBest(newPopulation));
        }
        return solutions;
    }

    public List<Backpack> solveVariantMutationSwap(double mutationRate, double elitistRate) {
        List<Backpack> newPopulation = new ArrayList<>(population);
        List<Backpack> solutions = new LinkedList<>();
        for(int g = 0; g < this.nbGenerations; g++){
            boolean test = false;
            while (!test) {
                List<Couple> parents = selection();
                newPopulation = crossover(parents);
                for (int i = 0; i < newPopulation.size(); i++) {
                    if (Math.random() < mutationRate) {
                        newPopulation.set(i, mutationSwap(newPopulation.get(i)));
                    }
                    newPopulation.get(i).repair();
                }
                for(int j = 1; j <= populationSize * elitistRate; j++){
                    this.replace(newPopulation);
                }
                population = newPopulation;
                test = (getBest(population).isSolutionValid());
            }
            solutions.add(getBest(newPopulation));
        }
        return solutions;
    }

    public List<Backpack> solveVariantRepairByWeight(double mutationRate, double elitistRate) {
        List<Backpack> newPopulation = new ArrayList<>(population);
        List<Backpack> solutions = new LinkedList<>();
        for(int g = 0; g < this.nbGenerations; g++){
            boolean test = false;
            while (!test) {
                List<Couple> parents = selection();
                newPopulation = crossover(parents);
                for (int i = 0; i < newPopulation.size(); i++) {
                    newPopulation.get(i).repairByWeight();
                }
                for (int i = 0; i < newPopulation.size(); i++) {
                    if (Math.random() < mutationRate) {
                        newPopulation.set(i, mutation(newPopulation.get(i)));
                    }
                    newPopulation.get(i).repairByWeight();
                }
                for(int j = 1; j <= populationSize * elitistRate; j++){
                    this.replace(newPopulation);
                }
                population = newPopulation;
                test = (getBest(population).isSolutionValid());
            }
            solutions.add(getBest(newPopulation));
        }
        return solutions;
    }

    private Backpack getBest(List<Backpack> population) {
        Backpack best = population.getFirst();
        for (Backpack b : population) {
            if (b != best && b.getFitness() > best.getFitness()) {
                best = b;
            }
        }
        return best;
    }

    private Backpack getWorst(List<Backpack> population) {
        Backpack worst = population.getFirst();
        for (Backpack b : population) {
            if (b != worst && b.getFitness() < worst.getFitness()){
                worst = b;
            }
        }
        return worst;
    }

    private void replace(List<Backpack> population) {
        Backpack worst = getWorst(population);
        Backpack best = getBest(population);
        population.remove(worst);
        population.add(best);
    }

    public double calculateMean(List<Integer> values) {
        double sum = 0.0;
        for (int value : values) {
            sum += value;
        }
        return sum / values.size();
    }

    public double calculateStandardDeviation(List<Integer> values) {
        double mean = calculateMean(values);
        double sumSquaredDiffs = 0.0;
        for (int value : values) {
            sumSquaredDiffs += Math.pow(value - mean, 2);
        }
        double variance = sumSquaredDiffs / values.size();
        return Math.sqrt(variance);
    }


    public List<Backpack> solveVariant(String s, String m, String c, String r, double mutationRate, double elitistRate) {
        List<Backpack> newPopulation = new ArrayList<>(population);
        List<Backpack> solutions = new LinkedList<>();
        for(int g = 0; g < this.nbGenerations; g++){
            List<Couple> parents;
            switch (s) {
                case "selection":
                    parents = selection();
                    break;
                case "tournament":
                    parents = selectionByTournament(TOURNAMENT_SIZE);
                    break;
                default:
                    parents = selection();
                    break;
            }
            switch (c) {
                case "crossover":
                    newPopulation = crossover(parents);
                    break;
                case "onepoint":
                    newPopulation = onePointCrossover(parents);
                    break;
                default:
                    newPopulation = crossover(parents);
                    break;
            }
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
