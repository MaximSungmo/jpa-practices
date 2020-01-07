package me.kickscar.practices.jpa03.model07.repository;

import me.kickscar.practices.jpa03.model07.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<User, String> {
}
