package com.dauphine.juliejoelle.algorithm;

public class Couple {
    private Backpack mother;
    private Backpack father;

    public Couple(Backpack mother, Backpack father) {
        this.mother = mother;
        this.father = father;
    }

    public Backpack getMother() {
        return mother;
    }

    public void setMother(Backpack mother) {
        this.mother = mother;
    }

    public Backpack getFather() {
        return father;
    }

    public void setFather(Backpack father) {
        this.father = father;
    }
}
