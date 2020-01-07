package me.kickscar.practices.jpa03.model08.domain.specs;

import me.kickscar.practices.jpa03.model08.domain.Blog;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class BlogSpecs {
    public static Specification<Blog> withNameContains(String keyword) {
        return new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(StringUtils.isEmpty(keyword)){
                    return null;
                }

                return criteriaBuilder.like(root.<String>get("name"), "%"+keyword+"%");
            }
        };
    }

    public static Specification<Blog> withUserNameContains(String keyword) {
        return new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(StringUtils.isEmpty(keyword)){
                    return null;
                }

                return criteriaBuilder.like(root.<String>get("user").get("name"), "%"+keyword+"%");
            }
        };
    }
}
