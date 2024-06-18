package com.dauphine.juliejoelle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {
    private Backpack backpack;
    private List<Item> items;
    private List<Backpack> population;
    private int populationSize;

    public GeneticAlgorithm(Backpack backpack, List<Item> items, int k) {
        this.items = items;
        this.population = new ArrayList<>();
        for(int i = 0; i < 2 * k; i++) {
            Backpack copy = new Backpack(backpack.getBudgets(), backpack.getObjects());
            copy.setSolution(this.generateRandomSolution(backpack.getObjects().size()));
            this.population.add(copy);
        }
        this.populationSize = k*2;
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
            selector.add(i);
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
            List<Boolean> newSolution = new ArrayList<>();
            for(int i=0; i<items.size()-1;i++){
                int whichParent = r.randomChoice(); // Math.random()
                if (whichParent==0){
                    newSolution.add(c.getFather().getSolution().get(i));
                }
                else{
                    newSolution.add(c.getMother().getSolution().get(i));
                }
            }
            Backpack b = new Backpack(backpack.getBudgets(), backpack.getObjects());
            b.setSolution(newSolution);
            newPop.add(b);
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

    private Backpack solve(double mutationRate, double elitistRate) {
        boolean test = false;
        int itnb = 0;
        List<Backpack> newPopulation = new ArrayList<>(population);
        while (!test) {
            List<Couple> parents = selection();
            newPopulation = crossover(parents);
            for (int i = 0; i < newPopulation.size(); i++) {
                if (Math.random() < mutationRate) {
                    newPopulation.set(i, mutation(newPopulation.get(i)));
                    newPopulation.get(i).repair();
                }
            }
            for(int j = 1; j <= populationSize * elitistRate; j++){
               this.replace(newPopulation);
            }
            population = newPopulation;
            test = (backpack.isSolutionValid()) || (itnb > 1000);
            itnb++;
        }
        return getBest(newPopulation);
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
