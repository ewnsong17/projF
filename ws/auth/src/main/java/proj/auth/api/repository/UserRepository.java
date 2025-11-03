package proj.auth.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proj.auth.api.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByName(String name);
}
