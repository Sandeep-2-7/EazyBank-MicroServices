package com.eazybytes.loans.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Data
@Schema(name = "Loans",
        description = "Schema to hold Loan information")
public class LoansDto {

    @NotEmpty(message = "Mobile Number cannot be empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile Number should be 10 digits")
    @Schema(description = "Mobile Number of Customer", example = "4365327698")
    private String mobileNumber;

    @NotEmpty(message = "Loan Number cannot be empty")
    @Pattern(regexp="(^$|[0-9]{12})",message = "LoanNumber must be 12 digits")
    @Schema(description = "Loan Number of the customer", example = "548732457654")
    private String loanNumber;

    @NotEmpty(message = "Loan Type cannot be empty")
    @Schema(description = "Type of the Loan", example = "Home Loan")
    private String loanType;

    @Positive(message = "Total Loan should be greater than zero")
    @Schema(description = "Total loan amount", example = "100000")
    private int totalLoan;

    @PositiveOrZero(message = "Total loan amount paid should be equal or greater than zero")
    @Schema(
            description = "Total loan amount paid", example = "1000"
    )
    private int amountPaid;

    @PositiveOrZero(message = "Total outstanding amount should be equal or greater than zero")
    @Schema(
            description = "Total outstanding amount against a loan", example = "99000"
    )
    private int outstandingAmount;
}
