package com.expensetracker.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetExpensesFilterDTO {

    private String category;
    private String sort; // e.g., "date_desc"
}
