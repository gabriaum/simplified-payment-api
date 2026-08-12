package com.gabriaum.picpay.transaction.factory;

import com.gabriaum.picpay.transaction.Transaction;
import com.gabriaum.picpay.transaction.TransactionEntity;
import com.gabriaum.picpay.transaction.dto.TransferDTO;
import com.gabriaum.picpay.user.UserEntity;

public class TransactionFactory {
    public static TransactionEntity createEntity(
            UserEntity payer,
            UserEntity payee,
            TransferDTO transferDTO
    ) {
       TransactionEntity entity = new TransactionEntity();
       entity.setPayer(payer);
       entity.setReceiver(payee);
       entity.setValue(transferDTO.value());

       return entity;
    }

    public static Transaction convertToResponse(TransactionEntity entity) {
        return new Transaction(
                entity.getId(),
                entity.getPayer().getId(),
                entity.getReceiver().getId(),
                entity.getValue(),
                entity.getDescription(),
                entity.getCreatedAt()
        );
    }
}