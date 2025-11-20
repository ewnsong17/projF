package proj.shared.model.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class LoginDTO {

    @Getter
    @Setter
    @ToString
    public static class LoginReq {
        @NotBlank public String id;
        @NotBlank public String pw;
    }


    @Getter
    @Setter
    @ToString
    @Builder
    public static class LoginRes {
        public String accessToken;
        public String refreshToken;
        public String sessionId;
        public String udpTicket;
        public String protocolVersion;
    }
}
