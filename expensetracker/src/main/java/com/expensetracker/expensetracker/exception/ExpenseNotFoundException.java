package com.expensetracker.expensetracker.exception;

import java.util.UUID;

public class ExpenseNotFoundException extends RuntimeException {

    public ExpenseNotFoundException(UUID expenseId, UUID userId) {
        super("Expense " + expenseId + " not found for X-User-Id: " + userId);
    }
}
