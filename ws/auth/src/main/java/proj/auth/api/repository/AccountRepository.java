package proj.auth.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proj.auth.api.entity.AccountEntity;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByName(String name);
}
