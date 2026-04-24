package com.expensetracker.expensetracker.service;

import com.expensetracker.expensetracker.model.Expense;
import com.expensetracker.expensetracker.dto.CreateExpenseDTO;
import com.expensetracker.expensetracker.dto.GetExpensesFilterDTO;

import java.util.List;

public interface ExpenseService {

    Expense createExpense(CreateExpenseDTO dto, String idempotencyKey);

    List<Expense> getExpenses(GetExpensesFilterDTO filters);
}
