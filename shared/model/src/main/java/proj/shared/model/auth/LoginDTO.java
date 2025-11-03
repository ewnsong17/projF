package proj.shared.model.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class LoginDTO {

    @Getter
    @Setter
    @Builder
    public static class LoginReq {
        @NotBlank public String id;
        @NotBlank public String pw;

        @Override
        public String toString() {
            return String.format("userId: %s, userPw: %s", id, pw);
        }
    }


    @Getter
    @Setter
    @Builder
    public static class LoginRes {
        public String accessToken;
        public String refreshToken;
        public String sessionId;
        public String udpTicket;
        public String protocolVersion;
    }
}
