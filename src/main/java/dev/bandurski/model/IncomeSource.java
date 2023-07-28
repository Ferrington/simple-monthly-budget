package dev.bandurski.model;

import java.math.BigDecimal;

public class IncomeSource {
    private int incomeSourceId;
    private int categoryId;
    private String name;
    private BigDecimal amount;

    public int getIncomeSourceId() {
        return incomeSourceId;
    }

    public void setIncomeSourceId(int incomeSourceId) {
        this.incomeSourceId = incomeSourceId;
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
        return "IncomeSource{" +
                "incomeSourceId=" + incomeSourceId +
                ", categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                '}';
    }
}
