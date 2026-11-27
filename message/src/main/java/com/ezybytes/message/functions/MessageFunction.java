package com.ezybytes.message.functions;

import com.ezybytes.message.dto.AccountMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class MessageFunction
{
    private static final Logger logger =  LoggerFactory.getLogger(MessageFunction.class);

    @Bean
    public Function<AccountMessageDto, AccountMessageDto> email(){
        return accountMessageDto -> {
            logger.info("sending email with the details : "+accountMessageDto.toString());
            return accountMessageDto;
        };
    }

    @Bean
    public Function<AccountMessageDto, Long> sms(){
        return accountMessageDto -> {
            logger.info("sending sms with the details : "+accountMessageDto.toString());
            return accountMessageDto.accountNumber();
        };
    }
}
