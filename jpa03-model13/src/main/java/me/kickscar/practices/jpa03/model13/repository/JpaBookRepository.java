package me.kickscar.practices.jpa03.model13.repository;

import me.kickscar.practices.jpa03.model13.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBookRepository extends JpaRepository<Book, Long> {
}
