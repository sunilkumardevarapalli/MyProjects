package com.eventledger.eventgateway.client;

import com.eventledger.eventgateway.dto.EventRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

@Component
public class AccountServiceClient {

    private final RestTemplate restTemplate;
    private static final String ACCOUNT_SERVICE_URL = "http://localhost:8081";

    public AccountServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @CircuitBreaker(name = "accountService", fallbackMethod = "handleAccountServiceDown")
    @Retry(name = "accountService")
    public void processTransaction(EventRequest request) {
        String url = ACCOUNT_SERVICE_URL + "/accounts/" + request.getAccountId() + "/transactions";
        restTemplate.postForObject(url, request, Object.class);
    }

    public void handleAccountServiceDown(EventRequest request, Exception ex) {
        throw new ServiceUnavailableException("Account Service is temporarily unavailable: " + ex.getMessage());
    }

    public static class ServiceUnavailableException extends RuntimeException {
        public ServiceUnavailableException(String message) {
            super(message);
        }
    }
}

