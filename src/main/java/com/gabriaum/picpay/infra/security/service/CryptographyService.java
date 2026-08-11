package com.gabriaum.picpay.infra.security.service;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class CryptographyService {
    public String encrypt(String value) {
        return BCrypt.hashpw(value, BCrypt.gensalt());
    }

    public Boolean check(String value, String hash) {
        return BCrypt.checkpw(value, hash);
    }
}