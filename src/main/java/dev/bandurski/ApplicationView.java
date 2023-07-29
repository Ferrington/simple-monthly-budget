package dev.bandurski;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import dev.bandurski.model.Expense;
import dev.bandurski.model.IncomeSource;
import dev.bandurski.model.Transaction;
import dev.bandurski.util.BasicConsole;
public class ApplicationView {

    private final BasicConsole console;

    public ApplicationView(BasicConsole console) {
        this.console = console;
    }

    public String getMenuSelection(String menuTitle, String[] options) {
        console.printBanner(menuTitle);
        return console.getMenuSelection(options);
    }

    public void displaySummary(List<IncomeSource> incomeSources, List<Expense> expenses) {
        displayIncomeSources(incomeSources);
        displayExpenses(expenses);

        BigDecimal netIncome = computeMonthlyNetIncome(incomeSources, expenses);

        console.printDivider();
        console.printBanner(String.format(
                "      %-10s  %s",
                "Net Income",
                netIncome
        ));
    }

    public void displayIncomeSources(List<IncomeSource> incomeSources) {
        displayIncomeSources(incomeSources, true);
    }

    public void displayIncomeSources(List<IncomeSource> incomeSources, boolean displayTotal) {
        int padding = calculatePaddingLength(incomeSources);

        String title = String.format(
                "Income Sources%n%4s  %-" + padding + "s  %s",
                "Id",
                "Name",
                "Amount"
        );
        console.printBanner(title);

        BigDecimal total = BigDecimal.ZERO;
        for (IncomeSource incomeSource: incomeSources) {
            console.printMessage(String.format(
                    "%4s  %-" + padding + "s  %s",
                    incomeSource.getIncomeSourceId(),
                    incomeSource.getName(),
                    incomeSource.getAmount()
            ));

            total = total.add(incomeSource.getAmount());
        }

        if (displayTotal) {
            console.printDivider();
            console.printMessage(String.format(
                    "      %-" + padding + "s  %s",
                    "Total",
                    total
            ));
        }
    }

    public IncomeSource promptForIncomeSource(IncomeSource existingSource) {
        IncomeSource newIncomeSource = new IncomeSource();
        if (existingSource == null) {
            newIncomeSource.setName(promptForName(null));
            newIncomeSource.setAmount(promptForAmount(null));
        } else {
            newIncomeSource.setIncomeSourceId(existingSource.getIncomeSourceId());
            newIncomeSource.setName(promptForName(existingSource.getName()));
            newIncomeSource.setAmount(promptForAmount(existingSource.getAmount()));
        }
        return newIncomeSource;
    }

    public IncomeSource selectIncomeSource(List<IncomeSource> sources) {
        while (true) {
            displayIncomeSources(sources, false);
            Integer sourceId = console.promptForInteger("Enter Income Source Id [0 to cancel]: ");
            if (sourceId == null || sourceId == 0) {
                return null;
            }
            IncomeSource selectedSource = findIncomeSourceById(sources, sourceId);
            if (selectedSource != null) {
                return selectedSource;
            }
            console.printErrorMessage("That's not a valid id. Please try again.");
        }
    }

    private IncomeSource findIncomeSourceById(List<IncomeSource> incomeSources, int incomeSourceId) {
        for (IncomeSource incomeSource : incomeSources) {
            if (incomeSource.getIncomeSourceId() == incomeSourceId) {
                return incomeSource;
            }
        }
        return null;
    }

    public void displayExpenses(List<Expense> expenses) {
        displayExpenses(expenses, true);
    }

    public void displayExpenses(List<Expense> expenses, boolean displayTotal) {
        int padding = calculatePaddingLength(expenses);

        String title = String.format(
                "Expenses%n%4s  %-" + padding + "s  %s",
                "Id",
                "Name",
                "Amount"
        );
        console.printBanner(title);

        BigDecimal total = BigDecimal.ZERO;
        for (Expense expense: expenses) {
            console.printMessage(String.format(
                    "%4s  %-" + padding + "s  %s",
                    expense.getExpenseId(),
                    expense.getName(),
                    expense.getAmount()
            ));

            total = total.add(expense.getAmount());
        }

        if (displayTotal) {
            console.printDivider();
            console.printMessage(String.format(
                    "      %-" + padding + "s  %s",
                    "Total",
                    total
            ));
        }
    }

    public Expense promptForExpense(Expense existingExpense) {
        Expense newExpense = new Expense();
        if (existingExpense == null) {
            newExpense.setName(promptForName(null));
            newExpense.setAmount(promptForAmount(null));
        } else {
            newExpense.setExpenseId(existingExpense.getExpenseId());
            newExpense.setName(promptForName(existingExpense.getName()));
            newExpense.setAmount(promptForAmount(existingExpense.getAmount()));
        }
        return newExpense;
    }

    public Expense selectExpense(List<Expense> expenses) {
        while (true) {
            displayExpenses(expenses, false);
            Integer expenseId = console.promptForInteger("Enter Expense Id [0 to cancel]: ");
            if (expenseId == null || expenseId == 0) {
                return null;
            }
            Expense selectedExpense = findExpenseById(expenses, expenseId);
            if (selectedExpense != null) {
                return selectedExpense;
            }
            console.printErrorMessage("That's not a valid id. Please try again.");
        }
    }

    private Expense findExpenseById(List<Expense> expenses, Integer expenseId) {
        for (Expense expense : expenses) {
            if (expense.getExpenseId() == expenseId) {
                return expense;
            }
        }
        return null;
    }

    private String promptForName(String defaultValue) {
        return promptForString("Name", true, defaultValue);
    }

    private BigDecimal promptForAmount(BigDecimal defaultValue) {
        return promptForBigDecimal("Amount", true, defaultValue);
    }

    private String promptWithDefault(String prompt, Object defaultValue) {
        if (defaultValue != null) {
            return prompt + "[" + defaultValue.toString() + "]: ";
        }
        return prompt + ": ";
    }

    private String promptForString(String prompt, boolean required, String defaultValue) {
        prompt = promptWithDefault(prompt, defaultValue);
        while (true) {
            String entry = console.promptForString(prompt);
            if (!entry.isEmpty()) {
                return entry;
            }
            // Entry is empty: see if we have a default, or if empty is OK (!required)
            if (defaultValue != null && !defaultValue.isEmpty()) {
                return defaultValue;
            }
            if (!required) {
                return entry;
            }
            console.printErrorMessage("A value is required, please try again.");
        }
    }

    private BigDecimal promptForBigDecimal(String prompt, boolean required, BigDecimal defaultValue) {
        prompt = promptWithDefault(prompt, defaultValue);
        while (true) {
            BigDecimal entry = console.promptForBigDecimal(prompt);
            if (entry != null) {
                return entry;
            }
            // Entry is empty: see if we have a default, or if empty is OK (!required)
            if (defaultValue != null) {
                return defaultValue;
            }
            if (!required) {
                return BigDecimal.valueOf(0.0);
            }
            console.printErrorMessage("A value is required, please try again.");
        }
    }

    private int calculatePaddingLength(List<? extends Transaction> transactions) {
        final int MIN_PADDING = 10;

        int padding = MIN_PADDING;

        for (Transaction transaction: transactions) {
            if (transaction.getName().length() > padding) {
                padding = transaction.getName().length();
            }
        }

        return padding;
    }

    private BigDecimal computeMonthlyNetIncome(List<IncomeSource> sources, List<Expense> expenses) {
        BigDecimal netIncome = BigDecimal.ZERO;

        for (IncomeSource source: sources) {
            netIncome = netIncome.add(source.getAmount());
        }

        for (Expense expense: expenses) {
            netIncome = netIncome.subtract(expense.getAmount());
        }

        return netIncome;
    }
}
