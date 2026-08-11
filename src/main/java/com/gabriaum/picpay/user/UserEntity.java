package com.gabriaum.picpay.user;

import com.gabriaum.picpay.user.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Table(name = "users")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    @Email(message = "Por favor, insira um email válido")
    private String email;

    @Column(unique = true, nullable = false, length = 20)
    private String cpf;

    @Column(nullable = false, check = @CheckConstraint(constraint = "LENGTH(password) >= 8"))
    private String password;

    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + role.name());
    }

    @PrePersist
    public void prePersist() {
        if (balance == null)
            balance = BigDecimal.ZERO;
    }
}