package dev.bandurski;

import dev.bandurski.dao.ExpenseDao;
import dev.bandurski.dao.IncomeSourceDao;
import dev.bandurski.dao.JdbcExpenseDao;
import dev.bandurski.dao.JdbcIncomeSourceDao;
import dev.bandurski.exception.DaoException;
import dev.bandurski.model.Expense;
import dev.bandurski.model.IncomeSource;
import dev.bandurski.util.BasicConsole;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.util.List;

public class ApplicationController {

    private final String DB_NAME = "monthly_budget";

    private final BasicConsole console;
    private final ApplicationView view;

    private final IncomeSourceDao incomeSourceDao;
    private final ExpenseDao expenseDao;

    public ApplicationController(BasicConsole console) {
        this.console = console;
        view = new ApplicationView(console);

        DataSource dataSource = setupDataSource(DB_NAME);
        incomeSourceDao = new JdbcIncomeSourceDao(dataSource);
        expenseDao = new JdbcExpenseDao(dataSource);
    }

    public void run() {
        displayMainMenu();
    }

    private void displayMainMenu() {
        final String SUMMARY = "Display Summary";
        final String INCOME = "Income Menu";
        final String EXPENSES = "Expense Menu";
        final String EXIT = "Exit";
        final String[] MENU_OPTIONS = {SUMMARY, INCOME, EXPENSES, EXIT};

        console.printBlankLine();
        console.printMessage("Monthly Budget Application");

        while (true) {
            console.printBlankLine();
            String title = "Main Menu";
            String selection = view.getMenuSelection(title, MENU_OPTIONS);
            if (selection.equals(SUMMARY)) {
                summary();
            } else if (selection.equals(INCOME)) {
                incomeMenu();
            } else if (selection.equals(EXPENSES)) {
                expensesMenu();
            } else {
                break;
            }
        }
    }

    private void summary() {

    }

    private void incomeMenu() {
        final String VIEW = "View income";
        final String CREATE = "Add new income source";
        final String UPDATE = "Modify income source";
        final String DELETE = "Delete income source";
        final String RETURN = "Return to Main Menu";
        final String[] MENU_OPTIONS = {VIEW, CREATE, UPDATE, DELETE, RETURN};

        while (true) {
            console.printBlankLine();
            String title = "Income Menu";
            String selection = view.getMenuSelection(title, MENU_OPTIONS);
            console.printDivider();
            console.printBlankLine();

            try {
                if (selection.equals(VIEW)) {
                    viewIncomeSources();
                } else if (selection.equals(CREATE)) {
                    createIncomeSource();
                } else if (selection.equals(UPDATE)) {
                    updateIncomeSource();
                } else if (selection.equals(DELETE)) {
                    deleteIncomeSource();
                } else {
                    break;
                }
            } catch (DaoException e) {
                console.printErrorMessage("DAO error - " + e.getMessage());
                throw e;
            }
        }
    }

    private void viewIncomeSources() {
        List<IncomeSource> incomeSources = incomeSourceDao.getIncomeSources();
        view.displayIncomeSources(incomeSources);
    }

    private void createIncomeSource() {
        IncomeSource newIncomeSource = view.promptForIncomeSource(null);

        if (newIncomeSource == null) {
            return;
        }

        newIncomeSource = incomeSourceDao.createIncomeSource(newIncomeSource);
        console.printMessage("Income Source " + newIncomeSource.getName() +
                " has been created.");
    }

    private void updateIncomeSource() {
        List<IncomeSource> sources = incomeSourceDao.getIncomeSources();

        if (sources.size() == 0) {
            console.printErrorMessage("There are no income sources to update!");
            return;
        }

        IncomeSource source = view.selectIncomeSource(sources);
        if (source == null) {
            return;
        }

        source = view.promptForIncomeSource(source);

        incomeSourceDao.updateIncomeSource(source);
        console.printMessage("Income source has been updated.");
    }

    private void deleteIncomeSource() {
        List<IncomeSource> sources = incomeSourceDao.getIncomeSources();
        if (sources.size() == 0) {
            console.printErrorMessage("There are no income sources to delete!");
            return;
        }

        IncomeSource source = view.selectIncomeSource(sources);

        if (source == null) {
            return;
        }

        boolean isConfirmed = console.promptForYesNo("Are you sure you want to delete this " +
                "income source? [y/n] ");
        if (!isConfirmed) {
            return;
        }

        incomeSourceDao.deleteIncomeSourceById(source.getIncomeSourceId());

        console.printMessage("Income source has been deleted.");
    }

    private void expensesMenu() {
        final String VIEW = "View expenses";
        final String CREATE = "Add new expense";
        final String UPDATE = "Modify expense";
        final String DELETE = "Delete expense";
        final String RETURN = "Return to Main Menu";
        final String[] MENU_OPTIONS = {VIEW, CREATE, UPDATE, DELETE, RETURN};

        while (true) {
            console.printBlankLine();
            String title = "Expense Menu";
            String selection = view.getMenuSelection(title, MENU_OPTIONS);
            console.printDivider();
            console.printBlankLine();

            try {
                if (selection.equals(VIEW)) {
                    viewExpenses();
                } else if (selection.equals(CREATE)) {
                    createExpense();
                } else if (selection.equals(UPDATE)) {
                    updateExpense();
                } else if (selection.equals(DELETE)) {
                    deleteExpense();
                } else {
                    break;
                }
            } catch (DaoException e) {
                console.printErrorMessage("DAO error - " + e.getMessage());
                throw e;
            }
        }
    }

    private void viewExpenses() {
        List<Expense> expenses = expenseDao.getExpenses();
        view.displayExpenses(expenses);
    }

    private void createExpense() {
        Expense newExpense = view.promptForExpense(null);

        if (newExpense == null) {
            return;
        }

        newExpense = expenseDao.createExpense(newExpense);
        console.printMessage("Expense " + newExpense.getName() +
                " has been created.");
    }

    private void updateExpense() {
        List<Expense> expenses = expenseDao.getExpenses();

        if (expenses.size() == 0) {
            console.printErrorMessage("There are no expenses to update!");
            return;
        }

        Expense expense = view.selectExpense(expenses);
        if (expense == null) {
            return;
        }

        expense = view.promptForExpense(expense);

        expenseDao.updateExpense(expense);
        console.printMessage("Expense has been updated.");
    }

    private void deleteExpense() {
        List<Expense> expenses = expenseDao.getExpenses();
        if (expenses.size() == 0) {
            console.printErrorMessage("There are no expenses to delete!");
            return;
        }

        Expense expense = view.selectExpense(expenses);

        if (expense == null) {
            return;
        }

        boolean isConfirmed = console.promptForYesNo("Are you sure you want to delete this expense? " +
                "[y/n] ");
        if (!isConfirmed) {
            return;
        }

        expenseDao.deleteExpenseById(expense.getExpenseId());

        console.printMessage("Expense has been deleted.");
    }

    private DataSource setupDataSource(String databaseName) {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setUrl(String.format("jdbc:postgresql://localhost:5432/" + databaseName));
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");
        return dataSource;
    }
}
