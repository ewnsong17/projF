package proj.auth.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthApiApplication {
    public static Logger logger = LoggerFactory.getLogger("debug");

    public static void main(String[] args) {
        logger.debug("auth server start..");
        SpringApplication.run(AuthApiApplication.class, args);
    }
}