package com.eazybytes.gatewayserver.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

public class FilterUtility {

    public static final String CORRELATION_ID = "eazybank-correlation-id";

    public String getCorrelationId(HttpHeaders headers) {
        if(headers.get(CORRELATION_ID) != null){
            List<String> correlationList = headers.get(CORRELATION_ID);
            return correlationList.stream().findFirst().get();
        }
        else return null;
    }

    public ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId) {
        return this.setRequestHeader(exchange, CORRELATION_ID, correlationId);
    }

    public ServerWebExchange setRequestHeader(ServerWebExchange exchange, String headerName, String headerValue) {
        return exchange.mutate().request(exchange.getRequest().mutate().header(headerName, headerValue).build()).build();
    }
}
