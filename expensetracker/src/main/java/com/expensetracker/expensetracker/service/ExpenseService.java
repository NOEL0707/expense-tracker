package com.expensetracker.expensetracker.service;

import com.expensetracker.expensetracker.model.Expense;
import com.expensetracker.expensetracker.dto.CreateExpenseDTO;
import com.expensetracker.expensetracker.dto.GetExpensesFilterDTO;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ExpenseService {

    Expense createExpense(UUID userId, CreateExpenseDTO dto, String idempotencyKey);

    Page<Expense> getExpenses(UUID userId, GetExpensesFilterDTO filters);

    Expense getExpense(UUID userId, UUID expenseId);

    Expense updateExpense(UUID userId, UUID expenseId, CreateExpenseDTO dto);

    void deleteExpense(UUID userId, UUID expenseId);
}
