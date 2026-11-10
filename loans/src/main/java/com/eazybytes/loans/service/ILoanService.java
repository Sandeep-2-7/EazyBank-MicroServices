package com.eazybytes.loans.service;

import com.eazybytes.loans.dto.LoansDto;
import jakarta.validation.constraints.Pattern;

public interface ILoanService {

    void createLoan(String mobileNumber);

    LoansDto fetchLoan(String mobileNumber);

    boolean updateLoan(LoansDto loansDto);

    boolean deleteLoan(String mobileNumber);
}
