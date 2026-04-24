package com.expensetracker.expensetracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateExpenseDTO {

    @NotNull
    @Positive
    private Integer amount;

    @NotBlank
    private String category;

    private String description;

    @NotNull
    private LocalDate date;
}
