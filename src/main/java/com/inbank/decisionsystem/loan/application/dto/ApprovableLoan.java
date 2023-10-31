package com.inbank.decisionsystem.loan.application.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApprovableLoan {

    private Float maxPossibleLoanAmount;

    private Integer maxSuitablePeriod;

}
