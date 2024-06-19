package com.dauphine.juliejoelle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {
    private Backpack backpack;
    private List<Item> items;
    private List<Backpack> population;
    private int populationSize;
    private int nbGenerations;


    public GeneticAlgorithm(Backpack backpack, List<Item> items, int k, int nbGenerations) {
        this.items = items;
        this.population = new ArrayList<>();
        for(int i = 0; i < 2 * k; i++) {
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
        for(int i = 0; i < size; i++) {
            solution.add(Math.random() < 0.5);
        }
        return solution;
    }

    // Variant possible
    private ArrayList<Couple> selection(){
        ArrayList<Couple> couples = new ArrayList<>();
        RandomSelector selector = new RandomSelector();
        for(int i = 0; i < populationSize; i++){
            selector.add(population.get(i).getFitness());
        }
        for(int i = 1; i <= populationSize/2; i++){
            int father = selector.randomChoice();
            int mother = selector.randomChoice();
            while(father == mother){
                mother = selector.randomChoice();
            }
            Couple couple = new Couple(population.get(mother), population.get(father));
            couples.add(couple);
        }
        return couples;
    }

    private List<Backpack> crossover(List<Couple> parents) {
        List<Backpack> newPop = new ArrayList<>();
        RandomSelector r = new RandomSelector();
        r.add(1); r.add(1);
        for(Couple c: parents){
            List<Boolean> newSolution1 = new ArrayList<>();
            List<Boolean> newSolution2 = new ArrayList<>();
            for(int i=0; i<items.size();i++){
                int whichParent = r.randomChoice(); // Math.random()
                if (whichParent==0){
                    newSolution1.add(c.getFather().getSolution().get(i));
                    newSolution2.add(c.getMother().getSolution().get(i));
                }
                else{
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

    public List<Backpack> solve(double mutationRate, double elitistRate) {
        List<Backpack> newPopulation = new ArrayList<>(population);
        List<Backpack> solutions = new LinkedList<>();
        for(int g = 0; g < this.nbGenerations; g++){
            boolean test = false;
            while (!test) {
                List<Couple> parents = selection();
                newPopulation = crossover(parents);
                for (int i = 0; i < newPopulation.size(); i++) {
                    if (Math.random() < mutationRate) {
                        newPopulation.set(i, mutation(newPopulation.get(i)));
                    }
//                    newPopulation.get(i).repair();
                    newPopulation.get(i).repairByWeight();
                }
                for(int j = 1; j <= populationSize * elitistRate; j++){
                    this.replace(newPopulation);
                }
                population = newPopulation;
                for(Backpack b: population){
                    System.out.println("Génération " + g +" : " + b.getSolution());
                }
                test = (getBest(population).isSolutionValid());
                //TODO faire itnb fois rajouter une solution dans population puis récupérer le best de la population à la fin
                //TODO faire solve plusieurs fois à la fin pour des statistiques
            }
            solutions.add(getBest(newPopulation));
        }
        System.out.println(solutions.size());
        System.out.println(solutions);
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


}
