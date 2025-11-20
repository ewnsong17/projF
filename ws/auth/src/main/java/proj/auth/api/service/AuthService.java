package proj.auth.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import proj.auth.api.config.AuthConfig;
import proj.auth.api.entity.AccountEntity;
import proj.auth.api.repository.AccountRepository;
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
    public static Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final AuthConfig prop;
    private final SecureRandom rand = new SecureRandom();
    private final AccountRepository accounts;
    private final JwtService jwtService;
    private final StringRedisTemplate redis;
    private final PasswordEncoder encoder;

    public AuthService(AuthConfig prop, AccountRepository accounts, PasswordEncoder encoder, JwtService jwtService, StringRedisTemplate redis) {
        this.prop = prop;
        this.accounts = accounts;
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
        logger.info("request info {}", req.toString());
        AccountEntity account = accounts.findByName(req.id).orElseThrow(() -> new IllegalArgumentException("INVALID_ACCOUNT"));
        if (!encoder.matches(req.getPw(), account.getPassword())) {
            throw new IllegalArgumentException("INVALID_ACCOUNT");
        }

        logger.info("find account {}|{}", account.getId(), account.getName());

        // random UUID 생성
        String sessionId = UUID.randomUUID().toString();

        logger.info("account {} get uuid: {}", account.getName(), sessionId);

        try {
            redis.opsForValue().set("session: " + sessionId, String.valueOf(account.getId()));
            logger.info("redis set ok for session {}", sessionId);
        } catch (Exception e) {
            logger.error("redis error", e);
            throw e;
        }

        // redis 에 TTL 저장
        redis.opsForValue().set("session: " + sessionId, String.valueOf(account.getId()));

        logger.info("add redis account info {}", account);

        // JWT 토큰 발급
        String access = jwtService.createAccessToken(String.valueOf(account.getId()), Map.of("name", account.getName()));
        String refresh = jwtService.createRefreshToken(String.valueOf(account.getId()));

        logger.info("get JWT bearer Token {}  |  {}", access, refresh);

        // UDP 1회성 티켓
        String udpTicket = "utk-"+getNonce(12);
        redis.opsForValue().set("udp:ticket:"+udpTicket, String.valueOf(account.getId()), 30, TimeUnit.SECONDS);
        
        return LoginRes.builder()
                .accessToken(access).refreshToken(refresh).udpTicket(udpTicket)
                .sessionId(sessionId).protocolVersion(prop.getProtocol()).build();
    }
}
