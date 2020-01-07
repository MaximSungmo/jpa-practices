package me.kickscar.practices.jpa03.model07.repository;

import me.kickscar.practices.jpa03.model07.domain.Blog;
import me.kickscar.practices.jpa03.model07.dto.BlogDto2;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaBlogRepository extends JpaRepository<Blog, Long>, JpaBlogQryDslRepository {
    List<BlogDto2> findAllByOrderByNoDesc();
}
