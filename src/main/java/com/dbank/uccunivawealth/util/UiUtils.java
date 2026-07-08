package com.dbank.uccunivawealth.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Small, stateless helper methods shared by several controllers: input validation,
 * money formatting, and alert dialogs. Kept as static utilities (not instantiable)
 * since they hold no state of their own.
 */
public final class UiUtils {

    private UiUtils() {}

    public static String formatMoney(double amount) {
        return String.format("GHS %,.2f", amount);
    }

    public static String requireNonEmpty(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
        return value.trim();
    }

    public static double parsePositiveOrZero(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            return 0.0;
        }
        try {
            double parsed = Double.parseDouble(value.trim());
            if (parsed < 0) {
                throw new IllegalArgumentException(fieldName + " cannot be negative.");
            }
            return parsed;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " must be a valid number.");
        }
    }

    /** A table cell that renders a Double balance with thousands separators and 2 decimals. */
    public static <S> TableCell<S, Double> moneyCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : String.format("%,.2f", value));
            }
        };
    }

    public static String generateAccountNumber() {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        int random = new SecureRandom().nextInt(10000);
        return "UW" + timestamp + String.format("%04d", random);
    }
}
