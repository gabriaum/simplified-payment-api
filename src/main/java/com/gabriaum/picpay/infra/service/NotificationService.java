package com.gabriaum.picpay.infra.service;

import com.gabriaum.picpay.infra.dto.NotificationDTO;
import com.gabriaum.picpay.infra.exception.NotificationUnavailableException;
import com.gabriaum.picpay.user.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final RestTemplate restTemplate;

    public void send(UserEntity user, String message) {
        NotificationDTO notificationDTO = new NotificationDTO(user.getEmail(), message);
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/notification", notificationDTO, String.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            log.error("Serviço de notificação indisponível. Código de status: {}", response.getStatusCode());
            throw new NotificationUnavailableException("Serviço de notificação indisponível. Código de status: " + response.getStatusCode());
        }

        log.info("Notificação enviada para o usuário {}: {}", user.getEmail(), message);
    }
}
