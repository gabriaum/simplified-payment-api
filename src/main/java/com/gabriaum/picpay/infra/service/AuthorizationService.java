package com.gabriaum.picpay.infra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthorizationService {
    private final RestTemplate restTemplate;

    public Boolean isAuthorized() {
        ResponseEntity<String> response = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", String.class);
        return response.getStatusCode() == HttpStatus.OK;
    }
}
