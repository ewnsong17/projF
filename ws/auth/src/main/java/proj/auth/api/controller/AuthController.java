package proj.auth.api.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proj.auth.api.service.AuthService;
import proj.shared.model.auth.LoginDTO;
import proj.shared.model.auth.ShakeDTO;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    public static Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /*
    클라이언트 쉐이크 요청
     */
    @PostMapping("/shake")
    public ResponseEntity<ShakeDTO.ShakeRes> shake() {
        logger.debug("request shake detected");
        return ResponseEntity.ok(authService.shake());
    }

    /*
    클라이언트 로그인 요청
     */
    @PostMapping("/login")
    public ResponseEntity<LoginDTO.LoginRes> login(@Valid @RequestBody LoginDTO.LoginReq req) {
        logger.debug("request login detected");
        return ResponseEntity.ok(authService.login(req));
    }
}
