package dev.bandurski.util;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface BasicConsole {

    void pauseOutput();

    void printMessage(String message);

    void printMessage(String message, boolean noLineFeed);

    void printErrorMessage(String message);

    void printBlankLine();

    void printBlankLines(int numberOfLines);

    void printDivider();

    void printBanner(String message);

    void printBulletedItems(String[] items);

    String getMenuSelection(String[] options);
    String getMenuSelection(String[] options, boolean allowNullResponse);

    Integer getMenuSelectionIndex(String[] options, boolean allowNullResponse);

    String promptForString(String prompt);
    boolean promptForYesNo(String prompt);
    Integer promptForInteger(String prompt);
    Double promptForDouble(String prompt);
    BigDecimal promptForBigDecimal(String prompt);
    LocalDate promptForLocalDate(String prompt);
}
