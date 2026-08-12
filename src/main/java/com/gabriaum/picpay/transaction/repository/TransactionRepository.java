package com.gabriaum.picpay.transaction.repository;

import com.gabriaum.picpay.transaction.TransactionEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {
    @Query("""
        SELECT transaction
        FROM TransactionEntity transaction
        WHERE transaction.payer.id = :userId
           OR transaction.receiver.id = :userId
        ORDER BY transaction.createdAt DESC
        """)
    List<TransactionEntity> findHistoryByUserId(@Param("userId") Long userId, Pageable pageable);

    List<TransactionEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}