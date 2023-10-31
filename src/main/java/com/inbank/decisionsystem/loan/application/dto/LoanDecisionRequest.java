package com.inbank.decisionsystem.loan.application.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanDecisionRequest {

    @NotEmpty(message = "{personal.code.required}")
    @Pattern(regexp = "^[0-9]{11}", message = "{invalid.personal.code}")
    private String personalCode;

    @DecimalMin(value = "2000", message = "{loan.amount.min}")
    @DecimalMax(value = "10000", message = "{loan.amount.max}")
    @Digits(integer = 5, fraction = 2)
    private Float loanAmount;

    @Min(value = 12, message = "{period.min}")
    @Max(value = 60, message = "{period.max}")
    private Integer periodInMonths;

}
