package me.kickscar.practices.jpa03.model08.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import me.kickscar.practices.jpa03.model08.domain.Blog;
import me.kickscar.practices.jpa03.model08.dto.BlogDto;
import me.kickscar.practices.jpa03.model08.domain.specs.BlogSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;

import static me.kickscar.practices.jpa03.model08.domain.QBlog.blog;
import me.kickscar.practices.jpa03.model08.dto.QBlogDto;


public class JpaBlogQryDslRepositoryImpl extends QuerydslRepositorySupport implements JpaBlogQryDslRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    public JpaBlogQryDslRepositoryImpl() {
        super(Blog.class);
    }

    @Override
    public Page<BlogDto> findAll2(Optional<String> blogName, Optional<String> userName, Pageable pageable) {

        // 1. 조건절 동적쿼리
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(blogName.isPresent()){
            booleanBuilder.and(blog.name.contains(blogName.get()));
        }
        if(userName.isPresent()){
            booleanBuilder.and(blog.user.name.contains(userName.get()));
        }

        // 2. 기본 쿼리 생성
        JPAQuery<BlogDto> query = queryFactory
                .select(new QBlogDto(blog.name, blog.user.id.as("userId"), blog.user.name.as("userName")))
                .from(blog)
                .innerJoin(blog.user)
                .where(booleanBuilder);

        // 3. 쿼리에 paging 적용
        if(Optional.ofNullable(pageable).isPresent()){
            query.offset(pageable.getOffset());
            query.limit(pageable.getPageSize());
            for(Sort.Order o : pageable.getSort()) {
                PathBuilder orderByExpression = new PathBuilder(Blog.class, "blog");
                query.orderBy(new OrderSpecifier(o.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC, orderByExpression.get(o.getProperty())));
            }
        }

        // 4. 쿼리 실행
        List<BlogDto> list = query.fetch();

        // 5. Paging이 적용안된 전체 Page 객체 반환
        if(!Optional.ofNullable(pageable).isPresent()) {
            return new PageImpl<BlogDto>(list);
        }

        // 6. Paging이 적용된 Page 객체 반환
        return new PageImpl<BlogDto>(list, pageable, queryFactory.from(blog).fetchCount());
    }

    @Override
    public Page<BlogDto> findAll3(Optional<String> blogName, Optional<String> userName, Pageable pageable) {
        // 1. 기본 쿼리 생성
        JPAQuery<BlogDto> query = queryFactory
                .select(new QBlogDto(blog.name, blog.user.id.as("userId"), blog.user.name.as("userName")))
                .from(blog)
                .innerJoin(blog.user)
                .where(blogName.isPresent() ? blog.name.contains(blogName.get()) : null, userName.isPresent() ? blog.user.name.contains(userName.get()) : null);

        // 2. 쿼리에 paging 적용
        if(Optional.ofNullable(pageable).isPresent()){
            query.offset(pageable.getOffset());
            query.limit(pageable.getPageSize());
            for(Sort.Order o : pageable.getSort()) {
                PathBuilder orderByExpression = new PathBuilder(Blog.class, "blog");
                query.orderBy(new OrderSpecifier(o.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC, orderByExpression.get(o.getProperty())));
            }
        }

        // 3. 쿼리 실행
        List<BlogDto> list = query.fetch();

        // 4. Paging이 적용안된 전체 Page 객체 반환
        if(!Optional.ofNullable(pageable).isPresent()) {
            return new PageImpl<BlogDto>(list);
        }

        // 5. Paging이 적용된 Page 객체 반환
        return new PageImpl<BlogDto>(list, pageable, queryFactory.from(blog).fetchCount());
    }

    @Override
    public Page<BlogDto> findAll4(Optional<String> blogName, Optional<String> userName, Pageable pageable) {
        // 1. 기본 쿼리 생성
        JPAQuery<BlogDto> query = queryFactory
                .select(new QBlogDto(blog.name, blog.user.id.as("userId"), blog.user.name.as("userName")))
                .from(blog)
                .innerJoin(blog.user)
                .where(blogName.isPresent() ? blog.name.contains(blogName.get()).and(userName.isPresent() ? blog.user.name.contains(userName.get()) : null) : null);

        // 2. 쿼리에 paging 적용
        if(Optional.ofNullable(pageable).isPresent()){
            query.offset(pageable.getOffset());
            query.limit(pageable.getPageSize());
            for(Sort.Order o : pageable.getSort()) {
                PathBuilder orderByExpression = new PathBuilder(Blog.class, "blog");
                query.orderBy(new OrderSpecifier(o.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC, orderByExpression.get(o.getProperty())));
            }
        }

        // 3. 쿼리 실행
        List<BlogDto> list = query.fetch();

        // 4. Paging이 적용안된 전체 Page 객체 반환
        if(!Optional.ofNullable(pageable).isPresent()) {
            return new PageImpl<BlogDto>(list);
        }

        // 5. Paging이 적용된 Page 객체 반환
        return new PageImpl<BlogDto>(list, pageable, queryFactory.from(blog).fetchCount());
    }
}