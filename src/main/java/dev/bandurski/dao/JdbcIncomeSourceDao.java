package dev.bandurski.dao;

import dev.bandurski.exception.DaoException;
import dev.bandurski.model.IncomeSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcIncomeSourceDao implements IncomeSourceDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcIncomeSourceDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<IncomeSource> getIncomeSources() {
        List<IncomeSource> incomeSources = new ArrayList<>();

        String sql = "SELECT income_source_id, name, amount FROM income_source ORDER BY amount DESC";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

            while (results.next()) {
                incomeSources.add(mapRowToIncomeSource(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database", e);
        }

        return incomeSources;
    }

    @Override
    public IncomeSource getIncomeSourceById(int incomeSourceId) {
        IncomeSource incomeSource = null;

        String sql = "SELECT income_source_id, name, amount FROM income_source " +
                "WHERE income_source_id = ?";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, incomeSourceId);

            if (results.next()) {
                incomeSource = mapRowToIncomeSource(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database", e);
        }

        return incomeSource;
    }

    @Override
    public IncomeSource createIncomeSource(IncomeSource incomeSource) {
        IncomeSource newIncomeSource = null;

        String sql = "INSERT INTO income_source (name, amount) " +
                "VALUES (?, ?) RETURNING income_source_id";

        try {
            Integer newId = jdbcTemplate.queryForObject(
                    sql,
                    Integer.class,
                    incomeSource.getName(),
                    incomeSource.getAmount()
            );
            if (newId != null) {
                newIncomeSource = getIncomeSourceById(newId);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database.", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return newIncomeSource;
    }

    @Override
    public IncomeSource updateIncomeSource(IncomeSource incomeSource) {
        IncomeSource updatedIncomeSource = null;

        String sql = "UPDATE income_source SET name = ?, amount = ? " +
                "WHERE income_source_id = ?";

        try {
            int rowsAffected = jdbcTemplate.update(
                    sql,
                    incomeSource.getName(),
                    incomeSource.getAmount(),
                    incomeSource.getIncomeSourceId()
            );

            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            } else {
                updatedIncomeSource = getIncomeSourceById(incomeSource.getIncomeSourceId());
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database.", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return updatedIncomeSource;
    }

    @Override
    public int deleteIncomeSourceById(int incomeSourceId) {
        String sql = "DELETE FROM income_source WHERE income_source_id = ?";

        try {
            return jdbcTemplate.update(sql, incomeSourceId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    private IncomeSource mapRowToIncomeSource(SqlRowSet results) {
        IncomeSource incomeSource = new IncomeSource();
        incomeSource.setIncomeSourceId(results.getInt("income_source_id"));
        incomeSource.setName(results.getString("name"));
        incomeSource.setAmount(results.getBigDecimal("amount"));

        return incomeSource;
    }
}
