package com.company.controller;

import com.company.service.AuthService;
import com.company.util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping("api/v1/email")
public class EmailController {
    private final AuthService authService;

    public EmailController(AuthService authService) {
        this.authService = authService;
    }

    @ApiOperation(value = "Email verification", notes="Method for email verification")
    @GetMapping("/verification/{token}")
    public ResponseEntity<String> login(@PathVariable String token) {
        Integer id = JwtUtil.decode(token);
        return ResponseEntity.ok(authService.emailVerification(id));
    }
}
