package com.gabriaum.picpay.infra;

import com.gabriaum.picpay.infra.security.service.CryptographyService;
import com.gabriaum.picpay.user.UserEntity;
import com.gabriaum.picpay.user.enums.Role;
import com.gabriaum.picpay.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInitializer implements CommandLineRunner {
    private final UserRepository repository;
    private final CryptographyService cryptographyService;

    @Value("${picpay.admin.first-name}")
    private String adminFirstName;

    @Value("${picpay.admin.last-name}")
    private String adminLastName;

    @Value("${picpay.admin.email}")
    private String adminEmail;

    @Value("${picpay.admin.cpf}")
    private String adminCpf;

    @Value("${picpay.admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            log.info("Gerando usuário administrativo...");

            UserEntity user = new UserEntity();
            user.setFirstName(adminFirstName);
            user.setLastName(adminLastName);
            user.setEmail(adminEmail);
            user.setCpf(adminCpf);
            user.setPassword(cryptographyService.encrypt(adminPassword));
            user.setRole(Role.MANAGER);

            repository.save(user);
            log.info("Usuário administrativo gerado com sucesso!");
        }
    }
}