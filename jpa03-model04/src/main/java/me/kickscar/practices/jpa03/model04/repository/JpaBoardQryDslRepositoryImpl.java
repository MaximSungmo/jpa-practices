package me.kickscar.practices.jpa03.model04.repository;

import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import me.kickscar.practices.jpa03.model04.domain.Board;
import me.kickscar.practices.jpa03.model04.dto.BoardDto;
import me.kickscar.practices.jpa03.model04.dto.CommentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static me.kickscar.practices.jpa03.model04.domain.QBoard.board;
import static me.kickscar.practices.jpa03.model04.domain.QComment.comment;

public class JpaBoardQryDslRepositoryImpl extends QuerydslRepositorySupport implements JpaBoardQryDslRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    public JpaBoardQryDslRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Board findById2(Long no) {
        return queryFactory
                .selectDistinct(board)
                .from(board)
                .innerJoin(board.user)
                .fetchJoin()
                .innerJoin(board.comments, comment)
                .fetchJoin()
                .where(board.no.eq(no))
                .fetchOne();
    }

    @Override
    public BoardDto findById3(Long no) {
        return queryFactory
                .select(Projections.fields(BoardDto.class, board.no, board.hit, board.title, board.contents, board.regDate, board.user.name.as("userName")))
                .from(board)
                .innerJoin(board.user)
                .where(board.no.eq(no))
                .fetchOne();
    }

    @Override
    public Board findById4(Long no) {
        return queryFactory
                .selectDistinct(board)
                .from(board)
                .innerJoin(board.user)
                .fetchJoin()
                .leftJoin(board.comments, comment)
                .fetchJoin()
                .leftJoin(comment.user)
                .fetchJoin()
                .where(board.no.eq(no))
                .fetchOne();
    }

    @Override
    public List<CommentDto> findCommentsByNo(Long no) {
        return queryFactory
                .select(Projections.bean(CommentDto.class, comment.no, comment.contents, comment.user.name.as("userName")))
                .from(board)
                .innerJoin(board.comments, comment)
                .innerJoin(comment.user)
                .where(board.no.eq(no))
                .fetch();
    }

    @Override
    public List<BoardDto> findAll3(Pageable pageable) {
        JPAQuery<BoardDto> query = queryFactory
                .select(Projections.fields(BoardDto.class, board.no, board.hit, board.title, board.contents, board.regDate, board.user.name.as("userName")))
                .from(board)
                .innerJoin(board.user);

        if (pageable != null) {
            query.offset(pageable.getOffset());
            query.limit(pageable.getPageSize());
            for (Sort.Order o : pageable.getSort()) {
                PathBuilder orderByExpression = new PathBuilder(Board.class, "board");
                query.orderBy(new OrderSpecifier(o.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC, orderByExpression.get(o.getProperty())));
            }
        }

        return query.fetch();
    }



}
