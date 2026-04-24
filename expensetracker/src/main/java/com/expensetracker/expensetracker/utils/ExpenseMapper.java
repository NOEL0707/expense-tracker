package com.expensetracker.expensetracker.utils;

import com.expensetracker.expensetracker.model.Expense;
import com.expensetracker.expensetracker.dto.CreateExpenseDTO;
import com.expensetracker.expensetracker.dto.ExpenseResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idempotencyKey", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Expense toEntity(CreateExpenseDTO dto);

    ExpenseResponseDTO toResponseDTO(Expense expense);
}
