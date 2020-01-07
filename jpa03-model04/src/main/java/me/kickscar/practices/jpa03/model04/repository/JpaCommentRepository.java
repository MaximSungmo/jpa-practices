package me.kickscar.practices.jpa03.model04.repository;

import me.kickscar.practices.jpa03.model04.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCommentRepository extends JpaRepository<Comment, Long>, JpaCommentQryDslRepository {
}
