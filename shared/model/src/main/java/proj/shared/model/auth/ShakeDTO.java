package proj.shared.model.auth;

public class ShakeDTO {
    public static class ShakeReq {
        public String clientNonce; // 클라이언트 난수값
        public String clientVersion; // 클라이언트 버전

        public ShakeReq(String clientNonce, String clientVersion) {
            this.clientNonce = clientNonce;
            this.clientVersion = clientVersion;
        }
    }

    public static class ShakeRes {
        public String serverNonce; // 서버 난수값
        public String protocolVersion; // 프로토콜 버전
        public String serverTime; // mills

    }
}
