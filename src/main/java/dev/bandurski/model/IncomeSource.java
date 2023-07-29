package dev.bandurski.model;

import java.math.BigDecimal;

public class IncomeSource implements Transaction {
    private int incomeSourceId;
    private String name;
    private BigDecimal amount;

    public int getIncomeSourceId() {
        return incomeSourceId;
    }

    public void setIncomeSourceId(int incomeSourceId) {
        this.incomeSourceId = incomeSourceId;
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
                ", name='" + name + '\'' +
                ", amount=" + amount +
                '}';
    }
}
