package dev.bandurski.dao;

import dev.bandurski.model.IncomeSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class JdbcIncomeSourceDao implements IncomeSourceDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcIncomeSourceDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<IncomeSource> getIncomeSources() {
        return null;
    }

    @Override
    public IncomeSource getIncomeSourceById(int incomeSourceId) {
        return null;
    }

    @Override
    public IncomeSource createIncomeSource(IncomeSource incomeSource) {
        return null;
    }

    @Override
    public IncomeSource updateIncomeSource(IncomeSource incomeSource) {
        return null;
    }

    @Override
    public int deleteIncomeSourceById(int incomeSourceId) {
        return 0;
    }
}
