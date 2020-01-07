package me.kickscar.practices.jpa03.model04.repository;

import me.kickscar.practices.jpa03.model04.config.JpaConfig;
import me.kickscar.practices.jpa03.model04.domain.*;
import me.kickscar.practices.jpa03.model04.dto.BoardDto;
import me.kickscar.practices.jpa03.model04.dto.CommentDto;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JpaConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JpaBoardRepositoryTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaBoardRepository boardRepository;

    @Autowired
    private JpaCommentRepository commentRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void test01Save(){
        User user1 = new User();
        user1.setName("둘리");
        user1.setPassword("1234");
        user1.setEmail("dooly@kickscar.me");
        user1.setGender(GenderType.MALE);
        user1.setRole(RoleType.USER);
        User user1Persisted = userRepository.save(user1);

        Board board1 = new Board();
        board1.setTitle("제목1");
        board1.setContents("내용1");
        board1.setUser(user1Persisted);
        boardRepository.save(board1);

        Board board2 = new Board();
        board2.setTitle("제목2");
        board2.setContents("내용2");
        board2.setUser(user1Persisted);
        boardRepository.save(board2);

        //==============================================================

        User user2 = new User();
        user2.setName("마이콜");
        user2.setPassword("1234");
        user2.setEmail("michol@kickscar.me");
        user2.setGender(GenderType.MALE);
        user2.setRole(RoleType.USER);
        User user2Persisted = userRepository.save(user2);

        Board board3 = new Board();
        board3.setTitle("제목3");
        board3.setContents("내용3");
        board3.setUser(user2Persisted);
        boardRepository.save(board3);

        Board board4 = new Board();
        board4.setTitle("제목4");
        board4.setContents("내용4");
        board4.setUser(user2Persisted);
        boardRepository.save(board4);

        //==============================================================

        User user3 = new User();
        user3.setName("또치");
        user3.setPassword("1234");
        user3.setEmail("ddochi@kickscar.me");
        user3.setGender(GenderType.MALE);
        user3.setRole(RoleType.USER);
        User user3Persisted = userRepository.save(user3);

        User user4 = new User();
        user4.setName("도우넛");
        user4.setPassword("1234");
        user4.setEmail("donut@kickscar.me");
        user4.setGender(GenderType.MALE);
        user4.setRole(RoleType.USER);
        userRepository.save(user4);

        commentRepository.save(1L, new Comment(user1Persisted, "댓글1"));
        commentRepository.save(2L, new Comment(user1Persisted, "댓글2"), new Comment(user2Persisted, "댓글3"));
        commentRepository.save(3L, new Comment(user1Persisted, "댓글4"), new Comment(user2Persisted, "댓글5"), new Comment(user3Persisted, "댓글6"));

        assertEquals(6L, commentRepository.count());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test02SaveEagerProblem01() {
        User userPersisted = userRepository.findById(4L).get();
        commentRepository.save(1L, new Comment(userPersisted, "댓글7"));
        assertEquals(7L, commentRepository.count());
    }

    @Test
    @Transactional
    public void test03BoardListLazyProblem() {
        Integer qryCount = 0;
        Long N = 2L;

        qryCount++;
        List<Board> boards = boardRepository.findAllByOrderByRegDateDesc();

        for(Board board : boards) {
            User user = board.getUser();

            if(!em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(user)){
                qryCount++;
            }
            System.out.println(user);
        }

        assertEquals(N+1, qryCount.longValue());
    }

    @Test
    public void test04BoardListLazyProblemSolved(){
        final Integer size = 2;
        Integer page = 0;
        List<BoardDto> boardDtos = null;

        boardDtos = boardRepository.findAll3(PageRequest.of(page++, size, Sort.Direction.DESC, "regDate"));
        assertEquals(2, boardDtos.size());

        boardDtos = boardRepository.findAll3(PageRequest.of(page++, size, Sort.Direction.DESC, "regDate"));
        assertEquals(2, boardDtos.size());

        boardDtos = boardRepository.findAll3(PageRequest.of(page++, size, Sort.Direction.DESC, "regDate"));
        assertEquals(0, boardDtos.size());
    }

    @Test
    @Transactional
    public void test05BoardViewLazyProblem01(){
        Board board = boardRepository.findById(1L).get();
        assertEquals("제목1", board.getTitle());

        User user = board.getUser();
        assertEquals("둘리", user.getName());

        List<Comment> comments = board.getComments();
        assertEquals(2L, comments.size());
    }

    @Test
    public void test06BoardViewLazyProblem02(){
        Board board = boardRepository.findById2(3L);
    }

    @Test
    public void test07BoardViewLazySolved01(){
        BoardDto board = boardRepository.findById3(4L);
        assertEquals("제목4", board.getTitle());

        List<CommentDto> comments = boardRepository.findCommentsByNo(4L);
        assertEquals(0L, comments.size());

        board.setComments(comments);
        System.out.println(board);
    }

    @Test
    public void test08BoardViewLazySolved02(){
        Board board4 = boardRepository.findById4(4L);
        assertNotNull(board4);

        Board board1 = boardRepository.findById4(1L);
        assertEquals(1L, board1.getNo().longValue());
        assertEquals("둘리", board1.getUser().getName());
        assertEquals(2L, board1.getComments().size());
    }
}