package proj.shared.model.auth;

public enum AuthStatus {

    NOT_LOGIN(0);

    public final int value;
    private AuthStatus(int status) {
        this.value = status;
    }
}
