package br.com.erudio.erudioapi.service;

import br.com.erudio.erudioapi.dto.v1.security.AccountCredentialsDto;
import br.com.erudio.erudioapi.dto.v1.security.TokenDto;
import br.com.erudio.erudioapi.repository.UserRepository;
import br.com.erudio.erudioapi.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<TokenDto> signIn(AccountCredentialsDto accountCredentialsDto) {
        try {
            var username = accountCredentialsDto.getUsername();
            var password = accountCredentialsDto.getPassword();
            System.out.println(username);
            System.out.println(password);
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            var user = userRepository.findByUsername(username);
            var tokenResponse = new TokenDto();
            if (user != null) {
                tokenResponse = jwtTokenProvider.createAccessToken(username, user.getRoles());
            } else {
                throw new UsernameNotFoundException("Username " + username + " not found!");
            }
            return ResponseEntity.ok().body(tokenResponse);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadCredentialsException("Invalid password!");
        }
    }

    public ResponseEntity<TokenDto> refreshToken(String username, String refreshToken) {
        var user = userRepository.findByUsername(username);
        var tokenResponse = new TokenDto();
        if (user != null) {
            tokenResponse = jwtTokenProvider.refreshToken(refreshToken);
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found!");
        }
        return ResponseEntity.ok().body(tokenResponse);
    }
}
