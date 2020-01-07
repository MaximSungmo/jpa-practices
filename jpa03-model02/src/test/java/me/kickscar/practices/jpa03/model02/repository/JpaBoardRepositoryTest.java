package me.kickscar.practices.jpa03.model02.repository;

import me.kickscar.practices.jpa03.model02.config.JpaConfig;
import me.kickscar.practices.jpa03.model02.domain.Board;
import me.kickscar.practices.jpa03.model02.domain.GenderType;
import me.kickscar.practices.jpa03.model02.domain.RoleType;
import me.kickscar.practices.jpa03.model02.domain.User;
import me.kickscar.practices.jpa03.model02.dto.BoardDto;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

        Board board3 = new Board();
        board3.setTitle("제목3");
        board3.setContents("내용3");
        board3.setUser(user1Persisted);
        boardRepository.save(board3);

        User user2 = new User();
        user2.setName("둘리");
        user2.setPassword("1234");
        user2.setEmail("dooly@kickscar.me");
        user2.setGender(GenderType.MALE);
        user2.setRole(RoleType.USER);
        User user2Persisted = userRepository.save(user2);

        Board board4 = new Board();
        board4.setTitle("제목4");
        board4.setContents("내용4");
        board4.setUser(user2Persisted);
        boardRepository.save(board4);

        Board board5 = new Board();
        board5.setTitle("제목5");
        board5.setContents("내용5");
        board5.setUser(user2Persisted);
        boardRepository.save(board5);

        assertEquals(5L, boardRepository.count());
    }

    @Test
    public void test02FindById(){
        Board board = boardRepository.findById(1L).get();
        assertEquals(1L, board.getNo().longValue());
        assertEquals(User.class, board.getUser().getClass());
    }

    @Test
    public void test03FindById2(){
        BoardDto boardDto = boardRepository.findById2(1L);
        assertEquals(1L, boardDto.getNo().longValue());
        assertEquals("둘리", boardDto.getUserName());
    }

    @Test
    public void test04FindAllByOrderByRegDateDesc(){
        List<Board> boards = boardRepository.findAllByOrderByRegDateDesc();
        assertEquals(5, boards.size());
    }

    @Test
    public void test05FindAllByOrderByRegDateDesc2(){
        List<Board> boards = boardRepository.findAllByOrderByRegDateDesc2();
        assertEquals(5, boards.size());
    }

    @Test
    public void test06FindAllByOrderByRegDateDesc3(){
        List<BoardDto> boardDtos = boardRepository.findAllByOrderByRegDateDesc3();
        assertEquals(5, boardDtos.size());
    }

    @Test
    public void test07FindAllByOrderByRegDateDesc3(){
        final Integer size = 3;
        Integer page = 0;
        List<BoardDto> boardDtos = null;

        boardDtos = boardRepository.findAllByOrderByRegDateDesc3(page++, size);
        assertEquals(3, boardDtos.size());

        boardDtos = boardRepository.findAllByOrderByRegDateDesc3(page++, size);
        assertEquals(2, boardDtos.size());

        boardDtos = boardRepository.findAllByOrderByRegDateDesc3(page++, size);
        assertEquals(0, boardDtos.size());
    }

    @Test
    public void test08FindAll3(){
        final Integer size = 3;
        Integer page = 0;
        List<BoardDto> boardDtos = null;

        boardDtos = boardRepository.findAll3(PageRequest.of(page++, size, Sort.Direction.DESC, "regDate"));
        assertEquals(3, boardDtos.size());

        boardDtos = boardRepository.findAll3(PageRequest.of(page++, size, Sort.Direction.DESC, "regDate"));
        assertEquals(2, boardDtos.size());

        boardDtos = boardRepository.findAll3(PageRequest.of(page++, size, Sort.Direction.DESC, "regDate"));
        assertEquals(0, boardDtos.size());
    }

    @Test
    public void test09FindAll3(){
        final Integer size = 3;
        final String keyword = "내용";
        Integer page = 0;
        List<BoardDto> boardDtos = null;

        boardDtos = boardRepository.findAll3(keyword, PageRequest.of(page++, size, Sort.Direction.DESC, "regDate"));
        assertEquals(3, boardDtos.size());

        boardDtos = boardRepository.findAll3(keyword, PageRequest.of(page++, size, Sort.Direction.DESC, "regDate"));
        assertEquals(2, boardDtos.size());

        boardDtos = boardRepository.findAll3(keyword, PageRequest.of(page++, size, Sort.Direction.DESC, "regDate"));
        assertEquals(0, boardDtos.size());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test10Update(){
        Board board = boardRepository.findById(1L).get();
        board.setTitle("제목10");
        board.setContents("내용10");

        assertEquals("둘리", board.getUser().getName());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test11Update(){
        Board board = new Board();
        board.setNo(2L);
        board.setTitle("제목20");
        board.setContents("내용20");

        assertTrue("둘리", boardRepository.update(board));
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test12Delete(){
        Board board = boardRepository.findById(1L).get();
        boardRepository.delete(board);
        assertEquals(4L, boardRepository.count());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test13Delete(){
        boardRepository.deleteById(2L);
        assertEquals(3L, boardRepository.count());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test14Delete(){
        boardRepository.delete(3L);
        assertEquals(2L, boardRepository.count());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test15Delete(){
        boardRepository.delete(4L, 2L);
        assertEquals(1L, boardRepository.count());
    }
}