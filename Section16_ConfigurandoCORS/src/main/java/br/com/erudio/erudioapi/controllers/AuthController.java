package br.com.erudio.erudioapi.controllers;

import br.com.erudio.erudioapi.dto.v1.security.AccountCredentialsDto;
import br.com.erudio.erudioapi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Endpoint")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    @Operation(summary = "Authenticate a user and return a token.")
    public ResponseEntity<?> signin(@RequestBody AccountCredentialsDto accountCredentialsDto) {
        if (hasNullParamSignIn(accountCredentialsDto)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Credentials!");
        }
        var token = authService.signIn(accountCredentialsDto);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Credentials!");
        }
        return token;
    }

    @PutMapping("/refresh/{username}")
    @Operation(summary = "Refresh a token.")
    public ResponseEntity<?> refreshToken(@PathVariable("username") String username,
                                          @RequestHeader("Authorization") String refreshToken) {
        if (hasNullParamRefreshToken(username, refreshToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Credentials!");
        }
        var token = authService.refreshToken(username, refreshToken);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Credentials!");
        }
        return token;
    }

    private boolean hasNullParamRefreshToken(String username, String refreshToken) {
        return refreshToken == null ||
                refreshToken.isBlank() ||
                username == null ||
                username.isBlank();
    }

    private boolean hasNullParamSignIn(AccountCredentialsDto accountCredentialsDto) {
        return accountCredentialsDto == null ||
                accountCredentialsDto.getUsername() == null ||
                accountCredentialsDto.getUsername().isBlank() ||
                accountCredentialsDto.getPassword() == null ||
                accountCredentialsDto.getPassword().isBlank();
    }
}
