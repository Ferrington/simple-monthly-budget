package dev.bandurski.dao;

import dev.bandurski.model.Expense;

import java.util.List;

public interface ExpenseDao {
    List<Expense> getExpenses();
    Expense getExpenseById(int expenseId);
    Expense createExpense(Expense expense);
    Expense updateExpense(Expense expense);
    int deleteExpenseById(int expenseId);
}
