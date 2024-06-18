package com.dauphine.juliejoelle;

import java.util.ArrayList;

/**
 * This class makes it possible to choose randomly an element
 * within a set where the probability of picking an element
 * is proportional to its value (fitness in the genetic algorithm).
 * @author Documented by Hugo Gilbert, a large part of the code
 * was written by David Eck and Julien Lesca
 *
 */
public class RandomSelector {
    private ArrayList<Float> proba;
    private float somme = 0;

    /**
     * This constructor creates a RandomSelector.
     */
    public RandomSelector() {
        this.proba = new ArrayList<Float>();
    }

    /**
     * This method adds an element to the set of values that
     * can be chosen from. In the context of the genetic algorithm
     * the value i is the fitness of some individual.
     * @param i, the fitness of the individual to be added to
     * the  RandomSelector.
     */
    public void add(int i) {
        this.proba.add(Float.valueOf(i));
        this.somme += i;
    }

    /**
     * Returns the index of some individual. Each index is chosen with
     * a probability which is proportional to the fitness of the individual.
     * @return the index of an individual chosen with a probability
     *  which is proportional to the fitness of the individual.
     */
    public int randomChoice() {
        double rand = Math.random() * this.somme;
        float cumul = 0;
        for(int j = 0; j < this.proba.size(); j++) {
            cumul += this.proba.get(j).floatValue();
            if(rand < cumul) {
                return j;
            }
        }
        return this.proba.size() - 1;
    }
}
