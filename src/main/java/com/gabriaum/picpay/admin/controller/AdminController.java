package com.gabriaum.picpay.admin.controller;

import com.gabriaum.picpay.admin.dto.UpdateUserRoleDTO;
import com.gabriaum.picpay.admin.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PatchMapping("/users/{userId}/role")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> updateUserRole(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserRoleDTO request
    ) {
        return adminService.updateUserRole(userId, request.role());
    }
}