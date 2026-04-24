package com.expensetracker.expensetracker.controller;

import com.expensetracker.expensetracker.model.Expense;
import com.expensetracker.expensetracker.dto.CreateExpenseDTO;
import com.expensetracker.expensetracker.dto.ExpenseResponseDTO;
import com.expensetracker.expensetracker.dto.GetExpensesFilterDTO;
import com.expensetracker.expensetracker.service.ExpenseService;
import com.expensetracker.expensetracker.utils.ExpenseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@Tag(name = "Expense API", description = "API for managing expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final ExpenseMapper expenseMapper;

    @PostMapping
    @Operation(summary = "Create a new expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Expense created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<ExpenseResponseDTO> createExpense(
            @Valid @RequestBody CreateExpenseDTO dto,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {

        Expense expense = expenseService.createExpense(dto, idempotencyKey);
        ExpenseResponseDTO response = expenseMapper.toResponseDTO(expense);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get list of expenses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expenses retrieved successfully")
    })
    public ResponseEntity<List<ExpenseResponseDTO>> getExpenses(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "sort", defaultValue = "date_desc") String sort) {

        GetExpensesFilterDTO filters = GetExpensesFilterDTO.builder()
                .category(category)
                .sort(sort)
                .build();

        List<Expense> expenses = expenseService.getExpenses(filters);
        List<ExpenseResponseDTO> response = expenses.stream()
                .map(expenseMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
