package com.dauphine.juliejoelle.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Backpack {
    private List<Integer> budgets;
    private List<Item> items;
    private List<Boolean> solution;

    public Backpack(List<Integer> budgets, List<Item> items) {
        this.budgets = budgets;
        this.items = items;
        this.solution = new ArrayList<>();
    }

    /**
     * Get the fitness of the solution
     * @return the fitness of the solution
     */
    public int getFitness() {
        int sum = 0;
        for (int i = 0; i < items.size(); i++) {
            if (solution.get(i)) {
                sum += items.get(i).getUtility();
            }
        }
        return sum;
    }

    /**
     * Check if the solution is valid
     * @return true if the solution is valid, false otherwise
     */
    public boolean isSolutionValid() {
        for(int i = 0; i < budgets.size(); i++) {
            int sum = 0;
            for (int j = 0; j < items.size(); j++) {
                if (solution.get(j)) {
                    sum += items.get(j).getCosts().get(i);
                }
            }
            if (sum > budgets.get(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Repair the solution if it is not valid
     */
    public void repair() {
        if (!this.isSolutionValid()) {
            List<Integer> indices = new LinkedList<>();
            for (int i = 0; i < items.size(); i++) {
                indices.add(i);
            }
            indices.sort(Comparator.comparingInt(i -> -items.get(i).getUtility()));

            List<Integer> totalCosts = new LinkedList<>();
            for (int j = 0; j < budgets.size(); j++) {
                int sum = 0;
                for (int i = 0; i < items.size(); i++) {
                    if (solution.get(i)) {
                        sum += items.get(i).getCosts().get(j);
                    }
                }
                totalCosts.add(sum);
            }

            // First Loop : remove items exceeds budget
            for (int l = indices.size() - 1; l > 0; l--) {
                for (int j = 0; j < budgets.size(); j++) {
                    if(solution.get(l) && totalCosts.get(j) > budgets.get(j)) {
                        solution.set(l, false);
                        for (int k = 0; k < budgets.size(); k++) {
                            int newCost = totalCosts.get(k) - items.get(l).getCosts().get(k);
                            totalCosts.set(k, newCost);
                        }
                    }
                }
            }

            // Second Loop : add items
            for (int l = 0; l < indices.size(); l++) {
                boolean canAdd = true;
                for (int j = 0; j < budgets.size(); j++) {
                    if (!solution.get(l) && totalCosts.get(j) + items.get(l).getCosts().get(j) > budgets.get(j)) {
                        canAdd = false;
                        break;
                    }
                }
                if(canAdd){
                    solution.set(l, true);
                    for (int k = 0; k < budgets.size(); k++) {
                        int newCost = totalCosts.get(k) + items.get(l).getCosts().get(k);
                        totalCosts.set(k, newCost);
                    }
                }
            }
        }
    }

    /**
     * Repair the solution if it is not valid by weight
     */
    public void repairByWeight() {
        if(!this.isSolutionValid()){
            List<Integer> indices = new LinkedList<>();
            for (int i = 0; i < items.size(); i++) {
                indices.add(i);
            }
            indices.sort(Comparator.comparingDouble(i -> -items.get(i).getWeight()));

            List<Integer> totalCosts = new LinkedList<>();
            for (int j = 0; j < budgets.size(); j++) {
                int sum = 0;
                for (int i = 0; i < items.size(); i++) {
                    if (solution.get(i)) {
                        sum += items.get(i).getCosts().get(j);
                    }
                }
                totalCosts.add(sum);
            }

            // First Loop : remove items exceeds budget
            for (int l = indices.size() - 1; l > 0; l--) {
                for (int j = 0; j < budgets.size(); j++) {
                    if(solution.get(l) && totalCosts.get(j) > budgets.get(j)) {
                        solution.set(l, false);
                        for (int k = 0; k < budgets.size(); k++) {
                            int newCost = totalCosts.get(k) - items.get(l).getCosts().get(k);
                            totalCosts.set(k, newCost);
                        }
                    }
                }
            }

            // Second Loop : add items
            for (int l = 0; l < indices.size(); l++) {
                boolean canAdd = true;
                for (int j = 0; j < budgets.size(); j++) {
                    if (!solution.get(l) && totalCosts.get(j) + items.get(l).getCosts().get(j) > budgets.get(j)) {
                        canAdd = false;
                        break;
                    }
                }
                if (canAdd) {
                    solution.set(l, true);
                    for (int k = 0; k < budgets.size(); k++) {
                        int newCost = totalCosts.get(k) + items.get(l).getCosts().get(k);
                        totalCosts.set(k, newCost);
                    }
                }
            }
        }
    }

    public List<Integer> getBudgets() {
        return budgets;
    }

    public void setBudgets(List<Integer> budgets) {
        this.budgets = budgets;
    }

    public List<Item> getObjects() {
        return items;
    }

    public void setObjets(List<Item> items) {
        this.items = items;
    }

    public List<Boolean> getSolution() {
        return solution;
    }

    public void setSolution(List<Boolean> solution) {
        this.solution = solution;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < solution.size(); i++) {
            if (solution.get(i))
                res.append("1");
            else
                res.append("0");
        }
        return res.toString();
    }
}
