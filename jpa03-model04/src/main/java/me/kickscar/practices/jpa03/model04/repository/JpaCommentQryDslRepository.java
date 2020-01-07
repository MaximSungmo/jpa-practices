package me.kickscar.practices.jpa03.model04.repository;

import me.kickscar.practices.jpa03.model04.domain.Comment;

public interface JpaCommentQryDslRepository {
    public void save(Long boardNo, Comment ...comment);
}
