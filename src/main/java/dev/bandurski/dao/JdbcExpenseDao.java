package dev.bandurski.dao;

import dev.bandurski.exception.DaoException;
import dev.bandurski.model.Expense;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcExpenseDao implements ExpenseDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcExpenseDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Expense> getExpenses() {
        List<Expense> expenses = new ArrayList<>();

        String sql = "SELECT expense_id, name, amount FROM expense ORDER BY amount DESC";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);

            while (results.next()) {
                expenses.add(mapRowToExpense(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database", e);
        }

        return expenses;
    }

    @Override
    public Expense getExpenseById(int expenseId) {
        Expense expense = null;

        String sql = "SELECT expense_id, name, amount FROM expense WHERE expense_id = ?";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, expenseId);

            if (results.next()) {
                expense = mapRowToExpense(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database", e);
        }

        return expense;
    }

    @Override
    public Expense createExpense(Expense expense) {
        Expense newExpense = null;

        String sql = "INSERT INTO expense (name, amount) " +
                "VALUES (?, ?) RETURNING expense_id";

        try {
            Integer newId = jdbcTemplate.queryForObject(
                    sql,
                    Integer.class,
                    expense.getName(),
                    expense.getAmount()
            );
            if (newId != null) {
                newExpense = getExpenseById(newId);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database.", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return newExpense;
    }

    @Override
    public Expense updateExpense(Expense expense) {
        Expense updatedExpense = null;

        String sql = "UPDATE expense SET name = ?, amount = ? " +
                "WHERE expense_id = ?";

        try {
            int rowsAffected = jdbcTemplate.update(
                    sql,
                    expense.getName(),
                    expense.getAmount(),
                    expense.getExpenseId()
            );

            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            } else {
                updatedExpense = getExpenseById(expense.getExpenseId());
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Could not connect to database.", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return updatedExpense;
    }

    @Override
    public int deleteExpenseById(int expenseId) {
        String sql = "DELETE FROM expense WHERE expense_id = ?";

        try {
            return jdbcTemplate.update(sql, expenseId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    private Expense mapRowToExpense(SqlRowSet results) {
        Expense expense = new Expense();
        expense.setExpenseId(results.getInt("expense_id"));
        expense.setName(results.getString("name"));
        expense.setAmount(results.getBigDecimal("amount"));

        return expense;
    }
}
