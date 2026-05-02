package rs.ac.metropolitan.subtrack.model;

import java.math.BigDecimal;

public class Budget {

    private BigDecimal monthlyBudget;
    private BigDecimal spentAmount;
    private BigDecimal remainingAmount;
    private int usedPercentage;

    public Budget() {
    }

    public Budget(BigDecimal monthlyBudget, BigDecimal spentAmount, BigDecimal remainingAmount, int usedPercentage) {
        this.monthlyBudget = monthlyBudget;
        this.spentAmount = spentAmount;
        this.remainingAmount = remainingAmount;
        this.usedPercentage = usedPercentage;
    }

    public BigDecimal getMonthlyBudget() {
        return monthlyBudget;
    }

    public void setMonthlyBudget(BigDecimal monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }

    public BigDecimal getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(BigDecimal spentAmount) {
        this.spentAmount = spentAmount;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public int getUsedPercentage() {
        return usedPercentage;
    }

    public void setUsedPercentage(int usedPercentage) {
        this.usedPercentage = usedPercentage;
    }
}
