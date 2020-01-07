package me.kickscar.practices.jpa03.model02.repository;

import me.kickscar.practices.jpa03.model02.config.JpqlConfig;
import me.kickscar.practices.jpa03.model02.domain.Board;
import me.kickscar.practices.jpa03.model02.domain.GenderType;
import me.kickscar.practices.jpa03.model02.domain.RoleType;
import me.kickscar.practices.jpa03.model02.domain.User;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JpqlConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QueryDslBoardRepositoryTest {

    @Autowired
    private QueryDslUserRepository userRepository;

    @Autowired
    private QueryDslBoardRepository boardRepository;

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
        userRepository.save(user1);

        Board board1 = new Board();
        board1.setTitle("제목1");
        board1.setContents("내용1");
        board1.setUser(user1);
        boardRepository.save(board1);

        Board board2 = new Board();
        board2.setTitle("제목2");
        board2.setContents("내용2");
        board2.setUser(user1);
        boardRepository.save(board2);

        Board board3 = new Board();
        board3.setTitle("제목3");
        board3.setContents("내용3");
        board3.setUser(user1);
        boardRepository.save(board3);

        User user2 = new User();
        user2.setName("둘리");
        user2.setPassword("1234");
        user2.setEmail("dooly@kickscar.me");
        user2.setGender(GenderType.MALE);
        user2.setRole(RoleType.USER);
        userRepository.save(user2);

        Board board4 = new Board();
        board4.setTitle("제목4");
        board4.setContents("내용4");
        board4.setUser(user2);
        boardRepository.save(board4);

        Board board5 = new Board();
        board5.setTitle("제목5");
        board5.setContents("내용5");
        board5.setUser(user2);
        boardRepository.save(board5);

        assertEquals(5L, boardRepository.count().longValue());
    }

    @Test
    public void test02FindById1(){
        Board board = boardRepository.findById1(1L);
        assertEquals(1L, board.getNo().longValue());
        assertEquals(User.class, board.getUser().getClass());
    }

    @Test
    public void test03FindById2(){
        Board board = boardRepository.findById2(1L);
        assertEquals(1L, board.getNo().longValue());
        assertEquals(User.class, board.getUser().getClass());
    }

    @Test
    public void test04FindAll1(){
        Integer page = 1;
        List<Board> list = boardRepository.findAll1();
        assertEquals(5, list.size());
    }

    @Test
    public void test05FindAll2(){
        Integer page = 1;
        List<Board> list = boardRepository.findAll2();
        assertEquals(5, list.size());
    }

    @Test
    public void test06FindAll3(){
        Integer page = 0;
        List<Board> list = boardRepository.findAll3();
        assertEquals(5, list.size());
    }

    @Test
    public void test07FindAll3(){
        Integer page = 0;

        List<Board> list1 = boardRepository.findAll3(page++, 3);
        assertEquals(3, list1.size());

        List<Board> list2 = boardRepository.findAll3(page++, 3);
        assertEquals(2, list2.size());

        List<Board> list3 = boardRepository.findAll3(page++, 3);
        assertEquals(0, list3.size());
    }

    @Test
    public void test08FindAll3(){
        final String keyword = "내용";
        Integer page = 0;

        List<Board> list1 = boardRepository.findAll3(keyword, page++, 3);
        assertEquals(3, list1.size());

        List<Board> list2 = boardRepository.findAll3(keyword, page++, 3);
        assertEquals(2, list2.size());

        List<Board> list3 = boardRepository.findAll3(keyword, page++, 3);
        assertEquals(0, list3.size());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test09Update1(){
        Board board = new Board();
        board.setNo(1L);
        board.setTitle("제목10");
        board.setContents("내용10");

        Board boardPersisted = boardRepository.update1(board);
        assertEquals("둘리", boardPersisted.getUser().getName());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test10Update2(){
        Board board = new Board();
        board.setNo(2L);
        board.setTitle("제목20");
        board.setContents("내용20");

        assertTrue(boardRepository.update2(board));
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test11Delete1(){
        boardRepository.delete1(1L);
        assertEquals(4L, boardRepository.count().longValue());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test12Delete2(){
        boardRepository.delete2(2L);
        assertEquals(3L, boardRepository.count().longValue());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test13Delete2(){
        boardRepository.delete2(3L, 1L);
        assertEquals(2L, boardRepository.count().longValue());
    }
}
