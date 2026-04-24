package com.expensetracker.expensetracker.controller;

import com.expensetracker.expensetracker.model.Expense;
import com.expensetracker.expensetracker.dto.CreateExpenseDTO;
import com.expensetracker.expensetracker.dto.ExpenseResponseDTO;
import com.expensetracker.expensetracker.dto.GetExpensesFilterDTO;
import com.expensetracker.expensetracker.dto.PagedResponseDTO;
import com.expensetracker.expensetracker.service.ExpenseService;
import com.expensetracker.expensetracker.utils.ExpenseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
@Validated
@Tag(name = "Expense API", description = "API for managing expenses")
public class ExpenseController {

    private static final String USER_ID_HEADER = "X-User-Id";

    private final ExpenseService expenseService;
    private final ExpenseMapper expenseMapper;

    @PostMapping
    @Operation(summary = "Create a new expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Expense created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<ExpenseResponseDTO> createExpense(
            @RequestHeader(USER_ID_HEADER) UUID userId,
            @Valid @RequestBody CreateExpenseDTO dto,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {

        Expense expense = expenseService.createExpense(userId, dto, idempotencyKey);
        ExpenseResponseDTO response = expenseMapper.toResponseDTO(expense);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get list of expenses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expenses retrieved successfully")
    })
    public ResponseEntity<PagedResponseDTO<ExpenseResponseDTO>> getExpenses(
            @RequestHeader(USER_ID_HEADER) UUID userId,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "sort", defaultValue = "date_desc") String sort,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) Integer page,
            @RequestParam(value = "size", defaultValue = "20") @Min(1) @Max(100) Integer size) {

        GetExpensesFilterDTO filters = GetExpensesFilterDTO.builder()
                .category(category)
                .sort(sort)
                .page(page)
                .size(size)
                .build();

        Page<Expense> expenses = expenseService.getExpenses(userId, filters);
        PagedResponseDTO<ExpenseResponseDTO> response = PagedResponseDTO.<ExpenseResponseDTO>builder()
                .content(expenses.getContent().stream().map(expenseMapper::toResponseDTO).toList())
                .page(expenses.getNumber())
                .size(expenses.getSize())
                .totalElements(expenses.getTotalElements())
                .totalPages(expenses.getTotalPages())
                .first(expenses.isFirst())
                .last(expenses.isLast())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{expenseId}")
    @Operation(summary = "Get a single expense for the supplied user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expense retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Expense not found")
    })
    public ResponseEntity<ExpenseResponseDTO> getExpense(
            @RequestHeader(USER_ID_HEADER) UUID userId,
            @PathVariable UUID expenseId) {

        Expense expense = expenseService.getExpense(userId, expenseId);
        return ResponseEntity.ok(expenseMapper.toResponseDTO(expense));
    }

    @PutMapping("/{expenseId}")
    @Operation(summary = "Update an existing expense for the supplied user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expense updated successfully"),
            @ApiResponse(responseCode = "404", description = "Expense not found")
    })
    public ResponseEntity<ExpenseResponseDTO> updateExpense(
            @RequestHeader(USER_ID_HEADER) UUID userId,
            @PathVariable UUID expenseId,
            @Valid @RequestBody CreateExpenseDTO dto) {

        Expense expense = expenseService.updateExpense(userId, expenseId, dto);
        return ResponseEntity.ok(expenseMapper.toResponseDTO(expense));
    }

    @DeleteMapping("/{expenseId}")
    @Operation(summary = "Delete an expense for the supplied user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Expense deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Expense not found")
    })
    public ResponseEntity<Void> deleteExpense(
            @RequestHeader(USER_ID_HEADER) UUID userId,
            @PathVariable UUID expenseId) {

        expenseService.deleteExpense(userId, expenseId);
        return ResponseEntity.noContent().build();
    }
}
