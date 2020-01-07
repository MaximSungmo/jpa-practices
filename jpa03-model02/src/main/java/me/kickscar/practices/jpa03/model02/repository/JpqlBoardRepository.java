package me.kickscar.practices.jpa03.model02.repository;

import me.kickscar.practices.jpa03.model02.domain.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class JpqlBoardRepository {

    @Autowired
    private EntityManager em;

    // 저장: 영속화
    public void save(Board board){
        em.persist(board);
    }

    // 삭제1: 영속객체
    public void delete1(Long no){
        Board board = em.find(Board.class, no);
        em.remove(board);
    }

    // 삭제2: JPQL
    public Boolean delete2(Long no){
        String qlString = "delete from Board b where b.no=:no";
        Query query = em.createQuery(qlString);
        query.setParameter("no", no);

        return query.executeUpdate() == 1;
    }

    // 삭제3: JPQL: 비즈니스 로직(게시물 번호와 사용자 번호로 삭제)
    public Boolean delete2(Long boardNo, Long userNo){
        String qlString = "delete from Board b where b.no = ?1 and b.user.no = ?2";
        Query query = em.createQuery(qlString);

        query.setParameter(1, boardNo);
        query.setParameter(2, userNo);

        return query.executeUpdate() == 1;
    }

    // 수정1:
    public Board update1(Board board){
        Board boardPersisted = em.find(Board.class, board.getNo());

        if(boardPersisted != null){
            boardPersisted.setTitle(board.getTitle());
            boardPersisted.setContents(board.getContents());
        }

        return boardPersisted;
    }

    // 수정2
    public Boolean update2(Board board){
        Query query = em.createQuery("update Board b set b.title=:title, b.contents=:contents where b.no=:no");

        query.setParameter("no", board.getNo());
        query.setParameter("title", board.getTitle());
        query.setParameter("contents", board.getContents());

        return query.executeUpdate() == 1;
    }

    // 조회1: Fetch One
    public Board findById1(Long no){
        return em.find(Board.class, no);
    }

    // 조회2: Fetch One
    public Board findById2(Long no){
        String qlString = "select b from Board b where b.no = :no";
        TypedQuery<Board> query = em.createQuery(qlString, Board.class);

        query.setParameter("no", no);

        return query.getSingleResult();
    }

    // 조회3: Fetch List
    public List<Board> findAll1(){
        String qlString = "select b from Board b order by b.regDate desc";
        TypedQuery<Board> query = em.createQuery(qlString, Board.class);

        return query.getResultList();
    }

    // 조회4: Inner Join List
    public List<Board> findAll2(){
        String qlString = "select b from Board b inner join b.user u order by b.regDate desc";
        TypedQuery<Board> query = em.createQuery(qlString, Board.class);

        return query.getResultList();
    }

    // 조회5: Fetch Join List
    public List<Board> findAll3(){
        String qlString = "select b from Board b join fetch b.user order by b.regDate desc";
        TypedQuery<Board> query = em.createQuery(qlString, Board.class);

        return query.getResultList();
    }

    // 조회6: Fetch Join List: Paging: 데이터 수는 3개씩
    public List<Board> findAll3(Integer page){
        String qlString = "select b from Board b join fetch b.user order by b.regDate desc";
        TypedQuery<Board> query = em.createQuery(qlString, Board.class);

        query.setFirstResult((page-1) * 3);
        query.setMaxResults(3);

        return query.getResultList();
    }

    // 조회7: Fetch Join List: Paging: 데이터 수는 3개씩: LIKE 검색
    public List<Board> findAll3(String keyword, Integer page){
        String qlString = "select b from Board b join fetch b.user where b.title like :keywordContains or b.contents like :keywordContains order by b.regDate desc";
        TypedQuery<Board> query = em.createQuery(qlString, Board.class);

        query.setParameter("keywordContains", "%" + keyword + "%");
        query.setFirstResult((page-1) * 3);
        query.setMaxResults(3);

        return query.getResultList();
    }

    // count
    public Long count(){
        String qlString = "select count(b) from Board b";
        TypedQuery<Long> query = em.createQuery(qlString, Long.class);
        return query.getSingleResult();
    }
}
