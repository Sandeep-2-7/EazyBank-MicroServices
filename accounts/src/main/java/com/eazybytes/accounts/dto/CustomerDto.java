package com.eazybytes.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(name = "CustomerDto",
        description = "Schema to hold Customer and Account information")
public class CustomerDto {

    @Schema(
            description = "Name of the customer", example = "Eazy Bytes"
    )
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 5, max = 30, message = "The length of the customer should be between 5 and 30")
    private String name;

    @Schema(
            description = "Email address of the customer", example = "tutor@eazybytes.com"
    )
    @NotEmpty(message = "email should not be empty")
    @Email(message = "Enter a valid email address")
    private String email;

    @Schema(
            description = "Mobile Number of the customer", example = "9133196166"
    )
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @Schema(
            description = "Account details of the Customer"
    )
    private AccountDto accountDto;
}
