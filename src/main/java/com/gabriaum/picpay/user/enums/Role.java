package com.gabriaum.picpay.user.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("Usuário"),
    SHOPKEEPER("Lojista"),
    MANAGER("Gerente")
    ;

    private final String name;

    @JsonValue
    public String getName() {
        return name;
    }

    public Boolean isInsertable() {
        return this == USER || this == SHOPKEEPER;
    }
}
