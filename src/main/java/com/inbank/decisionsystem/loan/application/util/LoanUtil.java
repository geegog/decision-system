package com.inbank.decisionsystem.loan.application.util;

import com.inbank.decisionsystem.loan.application.dto.LoanDecisionRequest;
import com.inbank.decisionsystem.loan.application.dto.ApprovableLoan;
import com.inbank.decisionsystem.loan.application.dto.UserProfile;

import java.util.ArrayList;
import java.util.List;

public class LoanUtil {

    public static List<UserProfile> hardCodedProfile() {
        List<UserProfile> profiles = new ArrayList<>();

        profiles.add(new UserProfile("49002010965", true, null));
        profiles.add(new UserProfile("49002010976", false, 1));
        profiles.add(new UserProfile("49002010987", false, 2));
        profiles.add(new UserProfile("49002010998", false, 3));

        return profiles;
    }

    public static float calculateCreditScore(int creditModifier, Float loanAmount, Integer loanPeriod) {
        return (creditModifier / loanAmount) * loanPeriod;
    }

    public static ApprovableLoan maximumLoanAmount(int segmentation) {
        if (segmentation == 1) {
            return new ApprovableLoan(6000F, 60);
        }
        return new ApprovableLoan(10000F, 60);
    }

    public static int getProfileCreditModifier(UserProfile userProfile) {
        if (userProfile.getSegmentation() == 1) {
            return 100;
        } else if (userProfile.getSegmentation() == 2) {
            return 300;
        } else {
            return 1000;
        }
    }

    public static ApprovableLoan maximumSum(float score, UserProfile userProfile, LoanDecisionRequest loanDecisionRequest) {
        if (canApproveLoan(score)) {
            return maximumLoanAmount(userProfile.getSegmentation());
        } else {
            return calculateSuitableLoan(getProfileCreditModifier(userProfile), loanDecisionRequest.getLoanAmount(),
                    loanDecisionRequest.getPeriodInMonths());
        }
    }

    public static boolean canApproveLoan(float creditScore) {
        return !(creditScore < 1);
    }

    public static ApprovableLoan calculateSuitableLoan(int modifier, float requestedAmount, int requestedPeriod) {
        double epsilon = 1e-9; // A small value to account for potential floating-point precision issues
        float amountStep = 25;
        int periodStep = 1;

        for (float amount = requestedAmount; amount >= 2000 && amount <= 10000; amount -= amountStep) {
            for (int period = requestedPeriod; period <= 60; period += periodStep) {
                // Evaluate your formula here
                double result = calculateCreditScore(modifier, amount, period);

                // Check if the result is very close to 1
                if (Math.abs(result - 1.0) < epsilon) {
                    return new ApprovableLoan(amount, period);
                }
            }
        }


        return null;
    }

    public static void main(String [] args) {

    }

}
