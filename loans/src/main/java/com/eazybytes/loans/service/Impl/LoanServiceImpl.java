package com.eazybytes.loans.service.Impl;

import com.eazybytes.loans.constants.LoanConstants;
import com.eazybytes.loans.dto.LoansDto;
import com.eazybytes.loans.entity.Loans;
import com.eazybytes.loans.exceptions.LoanAlreadyExistsException;
import com.eazybytes.loans.exceptions.ResourceNotFoundException;
import com.eazybytes.loans.mapper.LoansMapper;
import com.eazybytes.loans.repository.LoanRepository;
import com.eazybytes.loans.service.ILoanService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class LoanServiceImpl implements ILoanService {

    private LoanRepository loanRepository;

    @Override
    public void createLoan(String mobileNumber) {
        Optional<Loans> loan = loanRepository.findByMobileNumber(mobileNumber);

        if(loan.isPresent()) {
            throw new LoanAlreadyExistsException("Loan already exists with mobile number " + mobileNumber);
        }

        loanRepository.save(createNewLoan(mobileNumber));
    }

    @Override
    public LoansDto fetchLoan(String mobileNumber) {
        Loans loan = loanRepository.findByMobileNumber(mobileNumber).orElseThrow(() -> new ResourceNotFoundException("Loan does not exists with the mobile number "+mobileNumber));
        return LoansMapper.mapLoanToLoanDto(loan, new LoansDto());
    }

    @Override
    public boolean updateLoan(LoansDto loansDto) {
        Loans loans = loanRepository.findByLoanNumber(loansDto.getLoanNumber()).orElseThrow(() ->  new ResourceNotFoundException("Loan does not exists with the loan number"+loansDto.getLoanNumber()));
        LoansMapper.mapLoanDtoToLoans(loansDto, loans);
        loanRepository.save(loans);
        return true;
    }

    @Override
    public boolean deleteLoan(String mobileNumber) {
        Loans loan = loanRepository.findByMobileNumber(mobileNumber).orElseThrow(() -> new ResourceNotFoundException("Loan does not exists with mobile number "+mobileNumber));
        loanRepository.deleteById(loan.getLoanId());
        return true;
    }

    private Loans createNewLoan(String mobileNumber) {
        Loans newLoan = new Loans();
        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
        newLoan.setLoanNumber(Long.toString(randomLoanNumber));
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(LoanConstants.HOME_LOAN);
        newLoan.setTotalLoan(LoanConstants.NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(LoanConstants.NEW_LOAN_LIMIT);
        return newLoan;
    }
}
