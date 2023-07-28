package dev.bandurski.model;

import java.math.BigDecimal;

public class Expense {
    private int expenseId;
    private int categoryId;
    private String name;
    private BigDecimal amount;

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "expenseId=" + expenseId +
                ", categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                '}';
    }
}
