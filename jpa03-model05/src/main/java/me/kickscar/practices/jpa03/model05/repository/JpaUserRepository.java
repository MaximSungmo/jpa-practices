package me.kickscar.practices.jpa03.model05.repository;

import me.kickscar.practices.jpa03.model05.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<User, Long> {
}
