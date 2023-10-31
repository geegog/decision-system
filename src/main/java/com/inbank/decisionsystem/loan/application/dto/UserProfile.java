package com.inbank.decisionsystem.loan.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

    private String personalCode;

    private Boolean hasDebt;

    @Range(min = 1, max = 3)
    private Integer segmentation;

}
