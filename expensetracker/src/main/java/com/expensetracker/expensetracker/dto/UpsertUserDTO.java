package com.expensetracker.expensetracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpsertUserDTO {

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;
}
