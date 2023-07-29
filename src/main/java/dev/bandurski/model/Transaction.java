package dev.bandurski.model;

import java.math.BigDecimal;

public interface Transaction {
    public String getName();
    public BigDecimal getAmount();
}
