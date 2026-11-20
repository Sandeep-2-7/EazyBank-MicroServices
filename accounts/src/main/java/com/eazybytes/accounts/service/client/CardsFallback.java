package com.eazybytes.accounts.service.client;

import com.eazybytes.accounts.dto.CardsDto;
import com.eazybytes.accounts.dto.LoansDto;
import org.springframework.cloud.client.circuitbreaker.httpservice.HttpServiceFallback;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@HttpServiceFallback(value = CardsFallback.class, group = "cards")
public class CardsFallback {
    public ResponseEntity<CardsDto> fetchCard(String correlationId, String mobileNumber) {
        return null;
    }
}
