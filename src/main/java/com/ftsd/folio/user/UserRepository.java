package com.ftsd.folio.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);
  boolean existsByEmail(String email);
  Optional<User> findByResetPasswordToken(String token);

}
