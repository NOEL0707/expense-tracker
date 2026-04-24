package com.expensetracker.expensetracker.service;

import com.expensetracker.expensetracker.model.Expense;
import com.expensetracker.expensetracker.dto.CreateExpenseDTO;
import com.expensetracker.expensetracker.dto.GetExpensesFilterDTO;
import com.expensetracker.expensetracker.exception.ExpenseNotFoundException;
import com.expensetracker.expensetracker.model.AppUser;
import com.expensetracker.expensetracker.repository.ExpenseRepository;
import com.expensetracker.expensetracker.utils.ExpenseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;
    private final UserService userService;

    @Override
    @Transactional
    public Expense createExpense(UUID userId, CreateExpenseDTO dto, String idempotencyKey) {
        log.info("Creating expense for userId: {} with idempotencyKey: {}", userId, idempotencyKey);
        AppUser user = userService.getRequiredUser(userId);

        if (idempotencyKey != null) {
            Optional<Expense> existing = expenseRepository.findByUserIdAndIdempotencyKey(userId, idempotencyKey);
            if (existing.isPresent()) {
                log.info("Expense already exists for userId: {} and idempotencyKey: {}", userId, idempotencyKey);
                return existing.get();
            }
        }

        Expense expense = expenseMapper.toEntity(dto);
        expense.setIdempotencyKey(idempotencyKey);
        expense.setUser(user);
        Expense saved = expenseRepository.save(expense);

        log.info("Expense created with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Expense> getExpenses(UUID userId, GetExpensesFilterDTO filters) {
        log.info("Retrieving expenses for userId: {} with filters: {}", userId, filters);
        userService.getRequiredUser(userId);

        String category = filters != null ? filters.getCategory() : null;
        String sortParam = filters != null && filters.getSort() != null ? filters.getSort() : "date_desc";
        Sort sort = "date_asc".equals(sortParam) ? Sort.by(Sort.Direction.ASC, "date") : Sort.by(Sort.Direction.DESC, "date");
        List<Expense> expenses = expenseRepository.findExpensesWithFilters(userId, category, sort);

        log.info("Retrieved {} expenses", expenses.size());
        return expenses;
    }

    @Override
    @Transactional(readOnly = true)
    public Expense getExpense(UUID userId, UUID expenseId) {
        log.info("Retrieving expense {} for userId: {}", expenseId, userId);
        userService.getRequiredUser(userId);
        return expenseRepository.findByIdAndUserId(expenseId, userId)
                .orElseThrow(() -> new ExpenseNotFoundException(expenseId, userId));
    }

    @Override
    @Transactional
    public Expense updateExpense(UUID userId, UUID expenseId, CreateExpenseDTO dto) {
        log.info("Updating expense {} for userId: {}", expenseId, userId);
        Expense expense = getExpense(userId, expenseId);
        expense.setAmount(dto.getAmount());
        expense.setCategory(dto.getCategory());
        expense.setDescription(dto.getDescription());
        expense.setDate(dto.getDate());
        return expenseRepository.save(expense);
    }

    @Override
    @Transactional
    public void deleteExpense(UUID userId, UUID expenseId) {
        log.info("Deleting expense {} for userId: {}", expenseId, userId);
        Expense expense = getExpense(userId, expenseId);
        expenseRepository.delete(expense);
    }
}
