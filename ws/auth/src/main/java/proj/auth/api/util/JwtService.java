package proj.auth.api.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
public class JwtService {

    private final SecretKey key;
    private final long accessTTLSec;
    private final long refreshTTLSec;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-ttl-sec}") long accessTTLSec,
            @Value("${jwt.refresh-ttl-sec}") long refreshTTLSec
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTTLSec = accessTTLSec;
        this.refreshTTLSec = refreshTTLSec;
    }

    /*
    엑세스 토큰 생성
     */
    public String createAccessToken(String userId, Map<String, Object> claims) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(userId)
                .addClaims(claims)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(accessTTLSec)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /*
    토큰 만료 시 갱신 위한 리프레시 토큰 생성
     */
    public String createRefreshToken(String userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(refreshTTLSec)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /*
    토큰 만료 시키기
     */
    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }
}
