package proj.auth.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import proj.shared.model.auth.AuthStatus;

@Entity @Table(name = "user_info")
@Getter
@Setter
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private int status = AuthStatus.NOT_LOGIN.value;
}
