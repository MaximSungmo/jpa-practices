package me.kickscar.practices.jpa03.model11.repository;

import me.kickscar.practices.jpa03.model11.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBookRepository extends JpaRepository<Book, Long> {
}
