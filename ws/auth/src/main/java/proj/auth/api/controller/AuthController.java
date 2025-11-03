package proj.auth.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proj.auth.api.service.AuthService;
import proj.shared.model.auth.ShakeDTO;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /*
    클라이언트 쉐이크 요청
     */
    @PostMapping("/shake")
    public ResponseEntity<ShakeDTO.ShakeRes> shake(@RequestBody ShakeDTO.ShakeReq req) {
        return ResponseEntity.ok(authService.shake(req));
    }

    @PostMapping("/login")
    public ResponseEntity<ShakeDTO.ShakeRes> login(@Valid @RequestBody ShakeDTO.ShakeReq req) {
        return ResponseEntity.ok(authService.login(req));
    }
}
