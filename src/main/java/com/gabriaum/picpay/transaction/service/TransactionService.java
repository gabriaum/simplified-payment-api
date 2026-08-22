package com.gabriaum.picpay.transaction.service;

import com.gabriaum.picpay.infra.service.AuthorizationService;
import com.gabriaum.picpay.infra.service.NotificationService;
import com.gabriaum.picpay.transaction.Transaction;
import com.gabriaum.picpay.transaction.TransactionEntity;
import com.gabriaum.picpay.transaction.dto.TransferDTO;
import com.gabriaum.picpay.transaction.exception.UnauthorizedTransferException;
import com.gabriaum.picpay.transaction.exception.UserInsufficientBalanceException;
import com.gabriaum.picpay.transaction.factory.TransactionFactory;
import com.gabriaum.picpay.transaction.repository.TransactionRepository;
import com.gabriaum.picpay.user.UserEntity;
import com.gabriaum.picpay.user.enums.Role;
import com.gabriaum.picpay.user.exception.UserNotFoundException;
import com.gabriaum.picpay.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AuthorizationService authorizationService;
    private final NotificationService notificationService;

    @Transactional
    public Transaction transfer(
            UserEntity user,
            TransferDTO transferDTO
    ) {
        if (user.getBalance().compareTo(transferDTO.value()) < 0)
            throw new UserInsufficientBalanceException();

        UserEntity receiver = userRepository.findById(transferDTO.payeeId())
                .orElseThrow(UserNotFoundException::new);

        if (!receiver.getRole().equals(Role.SHOPKEEPER))
            throw new UnauthorizedTransferException("O destinatário da transferência deve ser um lojista.");

        if (!authorizationService.isAuthorized())
            throw new UnauthorizedTransferException("Serviço de autorização.");

        user.setBalance(user.getBalance().subtract(transferDTO.value()));
        receiver.setBalance(receiver.getBalance().add(transferDTO.value()));

        userRepository.save(user);
        userRepository.save(receiver);

        TransactionEntity transaction = TransactionFactory.createEntity(user, receiver, transferDTO);
        transactionRepository.save(transaction);

        notificationService.send(user, "Você realizou uma transferência de R$ " + transferDTO.value() + " para " + receiver.getUsername() + ".");
        notificationService.send(receiver, "Você recebeu uma transferência de R$ " + transferDTO.value() + " de " + user.getUsername() + ".");

        return TransactionFactory.convertToResponse(transaction);
    }

    @Transactional(readOnly = true)
    public List<Transaction> history(Long userId, Long range) {
        List<TransactionEntity> transactions;

        if (range == null) {
            transactions = findAllHistory(userId);
        } else {
            if (range <= 0) {
                throw new IllegalArgumentException(
                        "O range deve ser maior que zero."
                );
            }

            Pageable pageable = PageRequest.of(0, range.intValue());
            transactions = userId == null
                    ? transactionRepository.findAllByOrderByCreatedAtDesc(pageable)
                    : transactionRepository.findHistoryByUserId(userId, pageable);
        }

        return transactions.stream()
                        .map(TransactionFactory::convertToResponse)
                        .toList();
    }

    private List<TransactionEntity> findAllHistory(Long userId) {
        Pageable pageable = Pageable.unpaged();

        if (userId == null) {
            return transactionRepository.findAllByOrderByCreatedAtDesc(pageable);
        }

        userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return transactionRepository.findHistoryByUserId(userId, pageable);
    }
}