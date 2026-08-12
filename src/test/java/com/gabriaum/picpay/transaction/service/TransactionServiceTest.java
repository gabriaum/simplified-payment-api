package com.gabriaum.picpay.transaction.service;

import com.gabriaum.picpay.infra.service.AuthorizationService;
import com.gabriaum.picpay.infra.service.NotificationService;
import com.gabriaum.picpay.transaction.Transaction;
import com.gabriaum.picpay.transaction.dto.TransferDTO;
import com.gabriaum.picpay.transaction.exception.UnauthorizedTransferException;
import com.gabriaum.picpay.transaction.exception.UserInsufficientBalanceException;
import com.gabriaum.picpay.transaction.repository.TransactionRepository;
import com.gabriaum.picpay.user.UserEntity;
import com.gabriaum.picpay.user.enums.Role;
import com.gabriaum.picpay.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private TransactionService transactionService;

    private UserEntity payer;
    private UserEntity receiver;

    @BeforeEach
    void setUp() {
        payer = new UserEntity();
        payer.setId(1L);
        payer.setFirstName("Payer");
        payer.setLastName("User");
        payer.setEmail("payer@example.com");
        payer.setCpf("00011122233");
        payer.setPassword("pass");
        payer.setBalance(new BigDecimal("100.00"));
        payer.setRole(Role.USER);

        receiver = new UserEntity();
        receiver.setId(2L);
        receiver.setFirstName("Receiver");
        receiver.setLastName("Shop");
        receiver.setEmail("receiver@example.com");
        receiver.setCpf("99988877766");
        receiver.setPassword("pass");
        receiver.setBalance(new BigDecimal("50.00"));
        receiver.setRole(Role.SHOPKEEPER);
    }

    @Test
    void transferSuccess() {
        TransferDTO dto = new TransferDTO(new BigDecimal("10.00"), receiver.getId(), "desc");

        when(userRepository.findById(receiver.getId())).thenReturn(Optional.of(receiver));
        when(authorizationService.isAuthorized()).thenReturn(true);

        ResponseEntity<?> resp = transactionService.transfer(payer, dto);

        assertEquals(200, resp.getStatusCode().value());
        assertInstanceOf(Transaction.class, resp.getBody());

        // payer balance decreased and receiver increased
        assertEquals(new BigDecimal("90.00"), payer.getBalance());
        assertEquals(new BigDecimal("60.00"), receiver.getBalance());

        verify(userRepository, times(1)).save(payer);
        verify(userRepository, times(1)).save(receiver);
        verify(transactionRepository, times(1)).save(any());
        verify(notificationService, times(2)).send(any(), anyString());
    }

    @Test
    void transferInsufficientBalance() {
        TransferDTO dto = new TransferDTO(new BigDecimal("1000.00"), receiver.getId(), "desc");

        assertThrows(UserInsufficientBalanceException.class, () -> transactionService.transfer(payer, dto));

        verify(userRepository, never()).save(any());
    }

    @Test
    void transferReceiverNotShopkeeperReturnsBadRequest() {
        receiver.setRole(Role.USER);
        TransferDTO dto = new TransferDTO(new BigDecimal("10.00"), receiver.getId(), "desc");

        when(userRepository.findById(receiver.getId())).thenReturn(Optional.of(receiver));

        ResponseEntity<?> resp = transactionService.transfer(payer, dto);

        assertEquals(400, resp.getStatusCode().value());
        assertInstanceOf(String.class, resp.getBody());

        verify(userRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void transferUnauthorizedThrows() {
        TransferDTO dto = new TransferDTO(new BigDecimal("10.00"), receiver.getId(), "desc");

        when(userRepository.findById(receiver.getId())).thenReturn(Optional.of(receiver));
        when(authorizationService.isAuthorized()).thenReturn(false);

        assertThrows(UnauthorizedTransferException.class, () -> transactionService.transfer(payer, dto));

        verify(userRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }
}


