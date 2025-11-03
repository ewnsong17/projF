package proj.auth.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import proj.auth.api.config.AuthConfig;
import proj.auth.api.entity.UserEntity;
import proj.auth.api.repository.UserRepository;
import proj.shared.model.auth.ShakeDTO.*;
import proj.shared.model.auth.LoginDTO.*;
import proj.auth.api.util.JwtService;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {
    public static Logger logger = LoggerFactory.getLogger("debug");
    private final AuthConfig prop;
    private final SecureRandom rand = new SecureRandom();
    private final UserRepository users;
    private final JwtService jwtService;
    private final StringRedisTemplate redis;
    private final PasswordEncoder encoder;

    public AuthService(AuthConfig prop, UserRepository users, PasswordEncoder encoder, JwtService jwtService, StringRedisTemplate redis) {
        this.prop = prop;
        this.users = users;
        this.encoder = encoder;
        this.jwtService = jwtService;
        this.redis = redis;
    }
    
    /*
    서버 shake 처리
     */
    public ShakeRes shake() {
        String svrNonce = getNonce(16);
        String now = String.valueOf(Instant.now().toEpochMilli());
        return ShakeRes.builder()
                .serverNonce(svrNonce)
                .protocolVersion(prop.getProtocol())
                .serverTime(now)
                .build();
    }

    /*
    서버용 난수 생성
     */
    private String getNonce(int length) {
        byte[] buf = new byte[length];
        rand.nextBytes(buf);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
    }

    /*
    로그인 정보 처리
     */
    public LoginRes login(LoginReq req) {
        logger.info("user info {}", req.toString());
        UserEntity user = users.findByName(req.id).orElseThrow(() -> new IllegalArgumentException("INVALID_USER_INFO"));
        if (!encoder.matches(req.pw, user.getPassword())) {
            throw new IllegalArgumentException("INVALID_USER_INFO");
        }

        // random UUID 생성
        String sessionId = UUID.randomUUID().toString();

        // redis 에 TTL 저장
        redis.opsForValue().set("session: " + sessionId, String.valueOf(user.getId()));

        logger.info("user {} get uuid: {}", req.id, sessionId);

        // JWT 토큰 발급
        String access = jwtService.createAccessToken(String.valueOf(user.getId()), Map.of("name", user.getName()));
        String refresh = jwtService.createRefreshToken(String.valueOf(user.getId()));

        // UDP 1회성 티켓
        String udpTicket = "utk-"+getNonce(12);
        redis.opsForValue().set("udp:ticket:"+udpTicket, String.valueOf(user.getId()), 30, TimeUnit.SECONDS);
        
        return LoginRes.builder()
                .accessToken(access).refreshToken(refresh)
                .sessionId(sessionId).protocolVersion(prop.getProtocol()).build();
    }
}
