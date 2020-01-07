package me.kickscar.practices.jpa03.model02.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import me.kickscar.practices.jpa03.model02.domain.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import java.util.List;

import static me.kickscar.practices.jpa03.model02.domain.QBoard.board;

@Repository
public class QueryDslBoardRepository extends QuerydslRepositorySupport {

    @Autowired
    private JPAQueryFactory queryFactory;

    public QueryDslBoardRepository() {
        super(Board.class);
    }

    // 저장: 영속화
    public void save(Board board) {
        getEntityManager().persist(board);
    }

    // 삭제1: 영속객체
    public void delete1(Long no){
        Board board = getEntityManager().find(Board.class, no);
        getEntityManager().remove(board);
    }

    // 삭제2: JPQL
    public Boolean delete2(Long no) {
        return queryFactory
                .delete(board)
                .where(board.no.eq(no))
                .execute() == 1;
    }

    // 삭제3: JPQL: 비즈니스 로직(게시물 번호와 사용자 번호로 삭제)
    public Boolean delete2(Long boardNo, Long userNo) {
        return queryFactory
                .delete(board)
                // 다음 2개의 where 메소드는 완전 동일
                // .where(board.no.eq(boardNo).and(board.user.no.eq(userNo)))
                .where(board.no.eq(boardNo), board.user.no.eq(userNo))
                .execute() == 1;
    }

    // 수정1:
    public Board update1(Board board){
        Board boardPersisted = getEntityManager().find(Board.class, board.getNo());

        if(boardPersisted != null){
            boardPersisted.setTitle(board.getTitle());
            boardPersisted.setContents(board.getContents());
        }

        return boardPersisted;
    }

    // 수정2
    public Boolean update2(Board argBoard){
        return queryFactory
                .update(board)
                .set(board.title, argBoard.getTitle())
                .set(board.contents, argBoard.getContents())
                .where(board.no.eq(argBoard.getNo()))
                .execute() == 1;
    }

    // 조회1: Fetch One
    public Board findById1(Long no) {
        return getEntityManager().find(Board.class, no);
    }

    // 조회2: Fetch One
    public Board findById2(Long no) {
        return (Board) queryFactory
                .from(board)
                .where(board.no.eq(no))
                .fetchOne();
    }

    // 조회3: Fetch List
    public List<Board> findAll1() {
        return (List<Board>) queryFactory
                .select(board)
                .from(board)
                .orderBy(board.regDate.desc())
                .fetch();
    }

    // 조회4: Inner Join List
    public List<Board> findAll2() {
        return (List<Board>) queryFactory
                .select(board)
                .from(board)
                .innerJoin(board.user)
                .orderBy(board.regDate.desc())
                .fetch();
    }

    // 조회5: Fetch Join List
    public List<Board> findAll3() {
        return (List<Board>) queryFactory
                .select(board)
                .from(board)
                .innerJoin(board.user).fetchJoin()
                .orderBy(board.regDate.desc())
                .fetch();
    }

    // 조회6: Fetch Join List: Paging
    public List<Board> findAll3(Integer page, Integer size) {
        return (List<Board>) queryFactory
                .select(board)
                .from(board)
                .innerJoin(board.user).fetchJoin()
                .offset(page * size)
                .limit(size)
                .orderBy(board.regDate.desc())

                .fetch();
    }

    // 조회7: Fetch Join List: Paging: LIKE 검색
    public List<Board> findAll3(String keyword, Integer page, Integer size) {
        return (List<Board>) queryFactory
                .select(board)
                .from(board)
                .innerJoin(board.user).fetchJoin()
                .where(board.title.contains(keyword).or(board.contents.contains(keyword)))
                .orderBy(board.regDate.desc())
                .offset(page * size)
                .limit(size)
                .fetch();
    }

    // count
    public Long count() {
        return queryFactory
                .from(board)
                .fetchCount();
    }
}