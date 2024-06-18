package com.dauphine.juliejoelle;

public class Object {

    private int utility;
    private int[] costs;

    public Object(int utility, int[] costs) {
        this.utility = utility;
        this.costs = costs;
    }

    public int getUtility() {
        return utility;
    }

    public void setUtility(int utility) {
        this.utility = utility;
    }

    public int[] getCosts() {
        return costs;
    }

    public void setCosts(int[] costs) {
        this.costs = costs;
    }
}
