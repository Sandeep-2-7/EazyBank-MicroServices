package com.eazybytes.accounts.mapper;

import com.eazybytes.accounts.dto.AccountDto;
import com.eazybytes.accounts.entity.Accounts;

public class AccountsMapper {

    public static AccountDto mapToAccountDto(Accounts accounts, AccountDto accountDto){
        accountDto.setAccountNumber(accounts.getAccountNum());
        accountDto.setAccountType(accounts.getAccountType());
        accountDto.setBranchAddress(accounts.getBranchAddress());
        return accountDto;
    }

    public static Accounts mapToAccounts(AccountDto accountDto, Accounts accounts){
        accounts.setAccountNum(accountDto.getAccountNumber());
        accounts.setAccountType(accountDto.getAccountType());
        accounts.setBranchAddress(accountDto.getBranchAddress());
        return accounts;
    }
}
