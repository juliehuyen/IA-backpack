package com.dauphine.juliejoelle;

import java.util.List;

public class Backpack {
    private int[] budgets;
    private List<Object> objets;

    public Backpack(int[] budgets, List<Object> objets) {
        this.budgets = budgets;
        this.objets = objets;
    }

    public int[] getBudgets() {
        return budgets;
    }

    public void setBudgets(int[] budgets) {
        this.budgets = budgets;
    }

    public List<Object> getObjets() {
        return objets;
    }

    public void setObjets(List<Object> objets) {
        this.objets = objets;
    }
}
