package com.inbank.decisionsystem.loan.application.service;

import com.inbank.decisionsystem.loan.application.dto.LoanDecisionRequest;
import com.inbank.decisionsystem.loan.application.dto.LoanDecisionResponse;
import com.inbank.decisionsystem.loan.application.exception.BadRequestException;
import org.springframework.stereotype.Service;

import static com.inbank.decisionsystem.loan.application.util.LoanUtil.*;

@Service
public class LoanService {

    public LoanDecisionResponse handleLoanRequest(LoanDecisionRequest loanDecisionRequest) throws BadRequestException {

        var profiles = hardCodedProfile();

        var profile = profiles
                .stream()
                .filter(p -> p.getPersonalCode().equalsIgnoreCase(loanDecisionRequest.getPersonalCode()))
                .findFirst();

        if (profile.isEmpty()) {
            throw new BadRequestException("Please, provide personal code from test example");
        }

        var userProfile =  profile.get();

        if (userProfile.getHasDebt()) {
            return new LoanDecisionResponse(loanDecisionRequest, false, true, null);
        }

        var score = calculateCreditScore(getProfileCreditModifier(userProfile),
                loanDecisionRequest.getLoanAmount(), loanDecisionRequest.getPeriodInMonths());

        var maxSum = maximumSum(score, userProfile, loanDecisionRequest);


        return new LoanDecisionResponse(loanDecisionRequest, canApproveLoan(score), false, maxSum);

    }

}
