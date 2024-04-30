package org.itmo.eventapp.main.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${security.secret:HellMegaSecretKeyForItmoEventAppNoOneShouldKnowItKeepYourMouthShut}")
    private String SECRET;

    @Value("${security.jwt-token.lifetime:60}")
    private int MINUTES;

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(createSigningKey()).build()
            .parseClaimsJws(token).getBody();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUserLogin(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractUserLogin(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String generateToken(String login) {
        var now = Instant.now();

        return Jwts.builder()
            .setSubject(login)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plus(MINUTES, ChronoUnit.MINUTES)))
            .signWith(createSigningKey())
            .compact();
    }

    private Key createSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
    }
}
