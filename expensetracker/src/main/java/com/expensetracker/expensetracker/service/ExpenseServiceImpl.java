package com.expensetracker.expensetracker.service;

import com.expensetracker.expensetracker.model.Expense;
import com.expensetracker.expensetracker.dto.CreateExpenseDTO;
import com.expensetracker.expensetracker.dto.GetExpensesFilterDTO;
import com.expensetracker.expensetracker.repository.ExpenseRepository;
import com.expensetracker.expensetracker.utils.ExpenseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    @Override
    @Transactional
    public Expense createExpense(CreateExpenseDTO dto, String idempotencyKey) {
        log.info("Creating expense with idempotencyKey: {}", idempotencyKey);

        if (idempotencyKey != null) {
            Optional<Expense> existing = expenseRepository.findByIdempotencyKey(idempotencyKey);
            if (existing.isPresent()) {
                log.info("Expense already exists for idempotencyKey: {}", idempotencyKey);
                return existing.get();
            }
        }

        Expense expense = expenseMapper.toEntity(dto);
        expense.setIdempotencyKey(idempotencyKey);
        Expense saved = expenseRepository.save(expense);

        log.info("Expense created with id: {}", saved.getId());
        return saved;
    }

    @Override
    public List<Expense> getExpenses(GetExpensesFilterDTO filters) {
        log.info("Retrieving expenses with filters: {}", filters);

        String category = filters != null ? filters.getCategory() : null;
        String sortParam = filters != null && filters.getSort() != null ? filters.getSort() : "date_desc";
        Sort sort = "date_asc".equals(sortParam) ? Sort.by(Sort.Direction.ASC, "date") : Sort.by(Sort.Direction.DESC, "date");
        List<Expense> expenses = expenseRepository.findExpensesWithFilters(category, sort);

        log.info("Retrieved {} expenses", expenses.size());
        return expenses;
    }
}
