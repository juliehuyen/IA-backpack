package com.dauphine.juliejoelle;

import java.util.List;

public class Item {

    private int utility;
    private List<Integer> costs;
    private double weight;

    public Item(int utility, List<Integer> costs) {
        this.utility = utility;
        this.costs = costs;
        int sum = 0;
        for (int i = 0; i < costs.size(); i++) {
            sum += costs.get(i);
        }
        this.weight = utility / sum;
    }

    public int getUtility() {
        return utility;
    }

    public void setUtility(int utility) {
        this.utility = utility;
    }

    public List<Integer> getCosts() {
        return costs;
    }

    public void setCosts(List<Integer> costs) {
        this.costs = costs;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
