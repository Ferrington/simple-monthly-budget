package dev.bandurski;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

    private String defaultOnEnter(String response, String defaultValue) {
        if (response.isBlank()) {
            return defaultValue;
        }
        else {
            return response;
        }
    }

    private LocalDate defaultOnEnter(LocalDate response, LocalDate defaultValue) {
        if (response == null) {
            return defaultValue;
        }
        else {
            return response;
        }
    }

    private Integer defaultOnEnter(Integer response, Integer defaultValue) {
        if (response == null) {
            return defaultValue;
        }
        else {
            return response;
        }
    }

    private BigDecimal defaultOnEnter(BigDecimal response, BigDecimal defaultValue) {
        if (response == null) {
            return defaultValue;
        }
        else {
            return response;
        }
    }
}
