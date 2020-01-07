package me.kickscar.practices.jpa03.model02.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import me.kickscar.practices.jpa03.model02.domain.Board;
import me.kickscar.practices.jpa03.model02.dto.BoardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static me.kickscar.practices.jpa03.model02.domain.QBoard.board;

public class JpaBoardQryDslRepositoryImpl extends QuerydslRepositorySupport implements JpaBoardQryDslRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    public JpaBoardQryDslRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public BoardDto findById2(Long no) {
        return queryFactory
                .select(Projections.fields(BoardDto.class, board.no, board.hit, board.title, board.contents, board.regDate, board.user.name.as("userName")))
                .from(board)
                .innerJoin(board.user)
                .where(board.no.eq(no))
                .fetchOne();
    }

    @Override
    public List<Board> findAllByOrderByRegDateDesc2() {
        return (List<Board>) queryFactory
                .select(board)
                .from(board)
                .innerJoin(board.user).fetchJoin()
                .orderBy(board.regDate.desc())
                .fetch();
    }

    @Override
    public List<BoardDto> findAllByOrderByRegDateDesc3() {
        return queryFactory
                .select(Projections.fields(BoardDto.class, board.no, board.hit, board.title, board.contents, board.regDate, board.user.name.as("userName")))
                .from(board)
                .innerJoin(board.user)
                .orderBy(board.regDate.desc())
                .fetch();
    }

    @Override
    public List<BoardDto> findAllByOrderByRegDateDesc3(Integer page, Integer size) {
        return queryFactory
                .select(Projections.fields(BoardDto.class, board.no, board.hit, board.title, board.contents, board.regDate, board.user.name.as("userName")))
                .from(board)
                .innerJoin(board.user)
                .orderBy(board.regDate.desc())
                .offset(page * size)
                .limit(size)
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

    @Override
    public List<BoardDto> findAll3(String keyword, Pageable pageable) {
        JPAQuery<BoardDto> query = queryFactory
                .select(Projections.fields(BoardDto.class, board.no, board.hit, board.title, board.contents, board.regDate, board.user.name.as("userName")))
                .from(board)
                .innerJoin(board.user)
                .where(board.title.contains(keyword).or(board.contents.contains(keyword)));

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

    @Override
    public Boolean update(Board argBoard) {
        return queryFactory
                .update(board)
                .set(board.title, argBoard.getTitle())
                .set(board.contents, argBoard.getContents())
                .where(board.no.eq(argBoard.getNo()))
                .execute() == 1;
    }

    @Override
    public Boolean delete(Long no) {
        return queryFactory
                .delete(board)
                .where(board.no.eq(no))
                .execute() == 1;
    }

    @Override
    public Boolean delete(Long boardNo, Long userNo) {
        return queryFactory
                .delete(board)
                .where(board.no.eq(boardNo).and(board.user.no.eq(userNo)))
                .execute() == 1;
    }
}
