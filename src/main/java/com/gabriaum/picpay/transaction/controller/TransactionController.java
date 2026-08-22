package com.gabriaum.picpay.transaction.controller;

import com.gabriaum.picpay.transaction.dto.TransferDTO;
import com.gabriaum.picpay.transaction.service.TransactionService;
import com.gabriaum.picpay.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService service;

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> onTransfer(
            @AuthenticationPrincipal UserEntity user,
            @RequestBody TransferDTO transferDTO
    ) {
        return ResponseEntity.ok(service.transfer(user, transferDTO));
    }

    @GetMapping("/history")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> onAdminHistory(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "range", required = false) Long range
    ) {
        return ResponseEntity.ok(service.history(userId, range));
    }

    @GetMapping("/my-history")
    public ResponseEntity<?> onMyHistory(
            @AuthenticationPrincipal UserEntity user,
            @RequestParam(value = "range", required = false) Long range
    ) {
        return ResponseEntity.ok(service.history(user.getId(), range));
    }
}