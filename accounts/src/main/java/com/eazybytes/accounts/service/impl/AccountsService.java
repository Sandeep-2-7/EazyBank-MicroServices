package com.eazybytes.accounts.service.impl;

import com.eazybytes.accounts.constants.AccountConstant;
import com.eazybytes.accounts.dto.AccountDto;
import com.eazybytes.accounts.dto.AccountMessageDto;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.entity.Accounts;
import com.eazybytes.accounts.entity.Customer;
import com.eazybytes.accounts.exception.CustomerAlreadyExistsException;
import com.eazybytes.accounts.exception.ResourceNotFoundException;
import com.eazybytes.accounts.mapper.AccountsMapper;
import com.eazybytes.accounts.repository.AccountRepository;
import com.eazybytes.accounts.repository.CustomerRepository;
import com.eazybytes.accounts.service.IAccountsService;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import static com.eazybytes.accounts.mapper.CustomerMapper.mapToCustomer;
import static com.eazybytes.accounts.mapper.CustomerMapper.mapToCustomerDto;

@Service
@AllArgsConstructor
public class AccountsService implements IAccountsService {

    private static final Logger logger = LoggerFactory.getLogger(AccountsService.class);

    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;
    private MeterRegistry meterRegistry;
    private StreamBridge streamBridge;


    @Override
    public void createAccount(CustomerDto customerDto) {
        logger.debug("Creating customer and account");
        Customer customer = mapToCustomer(customerDto, new Customer());
        Optional<Customer> customerCheck = customerRepository.findByMobileNumber(customer.getMobileNumber());
        if(customerCheck.isPresent())
        {
            throw new CustomerAlreadyExistsException("Customer already exists with the mobile number" + customer.getMobileNumber());
        }

        Customer savedCustomer = customerRepository.save(customer);
        Accounts savedAccount = accountRepository.save(createNewAccount(savedCustomer));
        logger.info("Customer and account created successfully");
        meterRegistry.counter("accounts.operations", "operation", "created").increment();
        sendCommunication(savedAccount, savedCustomer);
    }

    private void sendCommunication(Accounts accounts, Customer customer){
        AccountMessageDto accountMessageDto =  new AccountMessageDto(accounts.getAccountNum(), customer.getName(), customer.getEmail(), customer.getMobileNumber());
        logger.info("Sending Communication request for the details: {}", accountMessageDto);
        var result = streamBridge.send("sendCommunication-out-0", accountMessageDto);
        logger.info("Is the Communication request successfully triggered ? : {}", result);
    }


    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        logger.debug("Fetching customer and account details");
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
        Accounts accounts = accountRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(() -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));

        CustomerDto customerDto = new CustomerDto();
        mapToCustomerDto(customer, customerDto);
        AccountDto accountDto = new AccountDto();
        customerDto.setAccountDto(AccountsMapper.mapToAccountDto(accounts, accountDto));
        logger.debug("Customer and account details fetched successfully");
        return customerDto;
    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        logger.debug("Updating customer and account details");
        boolean result = false;
        AccountDto accountDto = customerDto.getAccountDto();
        if(accountDto!=null){
            Accounts accounts = accountRepository.findById(accountDto.getAccountNumber()).orElseThrow(() -> new ResourceNotFoundException("Account", "accountNumber", accountDto.getAccountNumber().toString()));
            AccountsMapper.mapToAccounts(accountDto, accounts);
            accountRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer", "customerId", customerId.toString()));
            mapToCustomer(customerDto, customer);
            customerRepository.save(customer);

            result = true;
            logger.info("Customer and account details updated successfully");
        }
        meterRegistry.counter("accounts.operations", "operation", "updated").increment();
        return result;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        logger.debug("Deleting customer and account");
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
        Accounts account = accountRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(() -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));
        accountRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        //accountRepository.deleteById(account.getAccountNum());
        logger.info("Customer and account deleted successfully");
        meterRegistry.counter("accounts.operations", "operation", "deleted").increment();
        return true;
    }

    private Accounts createNewAccount(Customer savedCustomer){
        Accounts accounts = new Accounts();
        accounts.setCustomerId(savedCustomer.getCustomerId());
        long randomId = 1000000000L + new Random().nextInt(900000000);

        accounts.setAccountNum(randomId);
        accounts.setAccountType(AccountConstant.SAVINGS);
        accounts.setBranchAddress(AccountConstant.ADDRESS);
        return accounts;

    }
}
