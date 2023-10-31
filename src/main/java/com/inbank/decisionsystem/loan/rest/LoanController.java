package com.inbank.decisionsystem.loan.rest;

import com.inbank.decisionsystem.loan.application.dto.LoanDecisionRequest;
import com.inbank.decisionsystem.loan.application.exception.BadRequestException;
import com.inbank.decisionsystem.loan.application.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "${api.url.prefix}${api.version}/loan")
@CrossOrigin
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/decision")
    public ResponseEntity<?> loanRequest(
        @Valid @RequestBody LoanDecisionRequest request
    ) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(loanService.handleLoanRequest(request));
        } catch (BadRequestException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
}
