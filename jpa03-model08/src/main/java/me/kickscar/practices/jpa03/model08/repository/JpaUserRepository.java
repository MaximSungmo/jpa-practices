package me.kickscar.practices.jpa03.model08.repository;

import me.kickscar.practices.jpa03.model08.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<User, String> {
}
