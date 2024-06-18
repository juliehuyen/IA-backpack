package com.dauphine.juliejoelle;

import java.util.List;

public class Item {

    private int utility;
    private List<Integer> costs;

    public Item(int utility, List<Integer> costs) {
        this.utility = utility;
        this.costs = costs;
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
}
