package br.com.erudio.erudioapi.security.jwt;

import br.com.erudio.erudioapi.dto.v1.security.TokenDto;
import br.com.erudio.erudioapi.exceptions.InvalidJwtAuthenticationException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";

    @Value("${security.jwt.token.expired-length:3600000}")
    private Long expiredLengthMillis = 3600000L; //1h

    @Autowired
    private UserDetailsService userDetailsService;

    Algorithm algorithm = null;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public TokenDto createAccessToken(String username, List<String> roles) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + expiredLengthMillis); //plus an hour
        var accessToken = getAccessToken(username, roles, now, validity);
        var refreshToken = getRefreshToken(username, roles, now);

        return new TokenDto(
                username,
                true,
                now,
                validity,
                accessToken,
                refreshToken
        );
    }

    public TokenDto refreshToken(String refreshToken) {
        if (refreshToken.contains("Bearer ")) {
            refreshToken = refreshToken.substring("Bearer ".length());
        }
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);
        String username = decodedJWT.getSubject();
        List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
        return createAccessToken(username, roles);
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = decodedToken(token);
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(decodedJWT.getSubject());
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                "",
                userDetails.getAuthorities()
        );
    }

    public String resolveToken(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader("Authorization"); //Bearer {token}
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        DecodedJWT decodedJWT = decodedToken(token);
        try {
            return !decodedJWT.getExpiresAt().before(new Date());
        } catch (Exception e) {
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token!");
        }
    }

    private DecodedJWT decodedToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        return jwtVerifier.verify(token);
    }

    private String getAccessToken(String username, List<String> roles, Date now, Date validity) {
        String issuerUrl =
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .build()
                        .toUriString();

        return JWT
                .create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withSubject(username)
                .withIssuer(issuerUrl)
                .sign(algorithm);
    }

    private String getRefreshToken(String username, List<String> roles, Date now) {
        Date validity = new Date(now.getTime() + expiredLengthMillis * 3); //3h
        return JWT
                .create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withSubject(username)
                .sign(algorithm);
    }

}
