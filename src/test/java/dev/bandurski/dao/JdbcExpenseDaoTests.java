package dev.bandurski.dao;

import dev.bandurski.model.Expense;
import dev.bandurski.model.IncomeSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcExpenseDaoTests extends BaseDaoTests {

    private static final Expense EXPENSE_1;
    private static final Expense EXPENSE_2;
    private static final Expense EXPENSE_3;

    private static final List<Expense> ALL_EXPENSES = new ArrayList<>();

    static {
        EXPENSE_1 = mapValuesToExpense(1, "Rent", new BigDecimal("1550.52"));
        EXPENSE_2 = mapValuesToExpense(2, "Internet", new BigDecimal("77.77"));
        EXPENSE_3 = mapValuesToExpense(3, "Car Payment", new BigDecimal("244.44"));

        ALL_EXPENSES.add(EXPENSE_1);
        ALL_EXPENSES.add(EXPENSE_3);
        ALL_EXPENSES.add(EXPENSE_2);
    }

    private JdbcExpenseDao dao;

    @Before
    public void setup() {
        dao = new JdbcExpenseDao(dataSource);
    }

    @Test
    public void getExpenses_returns_sources_in_correct_order() {
        List<Expense> expenses = dao.getExpenses();
        Assert.assertEquals("getExpenses() returned incorrect number of expenses",
                ALL_EXPENSES.size(), expenses.size());

        for (int i = 0; i < ALL_EXPENSES.size(); i++) {
            assertExpensesMatch("expense at position " + i + " (zero indexed) " +
                    "did not match on column: ", ALL_EXPENSES.get(i), expenses.get(i));
        }
    }

    @Test
    public void getExpenseById_returns_correct_expense() {
        Expense expense = dao.getExpenseById(EXPENSE_3.getExpenseId());

        assertExpensesMatch(
            "getExpenseById(" + EXPENSE_3.getExpenseId() + ") did not match on column: ",
            EXPENSE_3,
            expense
        );
    }

    @Test
    public void createExpense_inserts_expense_with_expected_values() {
        Expense newExpense = mapValuesToExpense(999, "Balloons",
                new BigDecimal("45.99"));

        Expense createdExpense = dao.createExpense(newExpense);
        int newId = createdExpense.getExpenseId();
        Assert.assertNotEquals("expenseId not set when created, remained 0",
                0, newId);

        newExpense.setExpenseId(newId);
        Expense retrievedExpense = dao.getExpenseById(newId);
        assertExpensesMatch("created expense did not match on column: ",
                newExpense, retrievedExpense);
    }

    @Test
    public void updateExpense_updates_expense_with_expected_values() {
        Expense expenseToUpdate = mapValuesToExpense(EXPENSE_2.getExpenseId(),
                "Monkeys", new BigDecimal("9999.99"));

        dao.updateExpense(expenseToUpdate);
        Expense retrievedExpense = dao.getExpenseById(EXPENSE_2.getExpenseId());

        assertExpensesMatch("updated expense did not match on column: ",
                expenseToUpdate, retrievedExpense);
    }

    @Test
    public void deleteExpenseById_expense_can_no_longer_be_retrieved() {
        dao.deleteExpenseById(EXPENSE_1.getExpenseId());

        Expense retrievedSource = dao.getExpenseById(EXPENSE_1.getExpenseId());
        Assert.assertNull("deleted expense can still be retrieved", retrievedSource);
    }

    private static Expense mapValuesToExpense(int expenseId, String name,
                                                   BigDecimal amount) {
        Expense expense = new Expense();
        expense.setExpenseId(expenseId);
        expense.setName(name);
        expense.setAmount(amount);

        return expense;
    }

    private void assertExpensesMatch(String message, Expense expected, Expense actual) {
        Assert.assertEquals(message + "expense_id", expected.getExpenseId(),
                actual.getExpenseId());
        Assert.assertEquals(message + "name", expected.getName(),
                actual.getName());
        Assert.assertEquals(message + "amount", expected.getAmount(),
                actual.getAmount());
    }
}
