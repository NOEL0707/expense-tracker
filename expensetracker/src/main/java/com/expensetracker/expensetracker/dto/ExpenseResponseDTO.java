package com.expensetracker.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponseDTO {

    private UUID id;
    private Integer amount;
    private String category;
    private String description;
    private LocalDate date;
    private LocalDateTime createdAt;
}
