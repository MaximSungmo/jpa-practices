package me.kickscar.practices.jpa03.model08.repository;

import me.kickscar.practices.jpa03.model08.domain.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JpaBlogRepository extends JpaRepository<Blog, Long>, JpaBlogQryDslRepository, JpaSpecificationExecutor<Blog> {
    public Page<Blog> findAll(Specification<Blog> spec, Pageable pageable);
}
