package dev.bandurski.dao;

import dev.bandurski.model.IncomeSource;

import java.util.List;

public interface IncomeSourceDao {
    List<IncomeSource> getIncomeSources();
    IncomeSource getIncomeSourceById(int incomeSourceId);
    IncomeSource createIncomeSource(IncomeSource incomeSource);
    IncomeSource updateIncomeSource(IncomeSource incomeSource);
    int deleteIncomeSourceById(int incomeSourceId);
}
