package proj.auth.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class AuthConfig {
    private String protocol = "v1.0";
}
