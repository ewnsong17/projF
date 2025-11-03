package proj.auth.api.service;

import org.springframework.stereotype.Service;
import proj.auth.api.config.AuthProp;
import proj.shared.model.auth.ShakeDTO.*;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    private final AuthProp prop;
    private final SecureRandom rand = new SecureRandom();

    public AuthService(AuthProp prop) {
        this.prop = prop;
    }


    public ShakeRes shake(ShakeReq req) {
        String svrNonce = getNonce(16);
        String now = String.valueOf(Instant.now().toEpochMilli());
        return new ShakeRes(now, prop.getProtocol(), svrNonce);
    }

    /*
    서버용 난수 생성
     */
    private String getNonce(int length) {
        byte[] buf = new byte[length];
        rand.nextBytes(buf);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
    }
}
