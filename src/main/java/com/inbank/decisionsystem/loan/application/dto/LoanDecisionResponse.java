package com.inbank.decisionsystem.loan.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanDecisionResponse {

    private LoanDecisionRequest loanDecisionRequest;

    private Boolean canApproved;

    private Boolean hasDebt;

    private ApprovableLoan approvableLoan;

}
