package com.expensetracker.expensetracker.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    @Builder.Default
    @Min(0)
    private Integer page = 0;
    @Builder.Default
    @Min(1)
    @Max(100)
    private Integer size = 20;
}
