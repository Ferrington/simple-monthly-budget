package dev.bandurski;

import dev.bandurski.dao.IncomeSourceDao;
import dev.bandurski.dao.JdbcIncomeSourceDao;
import dev.bandurski.exception.DaoException;
import dev.bandurski.util.BasicConsole;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;

public class ApplicationController {

    private final String DB_NAME = "monthly_budget";

    private final BasicConsole console;
    private final ApplicationView view;

    private final IncomeSourceDao incomeSourceDao;

    public ApplicationController(BasicConsole console) {
        this.console = console;
        view = new ApplicationView(console);

        DataSource dataSource = setupDataSource(DB_NAME);
        incomeSourceDao = new JdbcIncomeSourceDao(dataSource);
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
                    view.displayIncome();
                }
            } catch (DaoException e) {
                console.printErrorMessage("DAO error - " + e.getMessage());
            }
        }
    }

    private void expensesMenu() {
    }

    private DataSource setupDataSource(String databaseName) {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setUrl(String.format("jdbc:postgresql://localhost:5432/" + databaseName));
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");
        return dataSource;
    }
}
