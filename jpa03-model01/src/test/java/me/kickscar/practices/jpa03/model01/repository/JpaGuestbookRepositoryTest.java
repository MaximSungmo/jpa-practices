package me.kickscar.practices.jpa03.model01.repository;

import me.kickscar.practices.jpa03.model01.config.JpaConfig;
import me.kickscar.practices.jpa03.model01.domain.Guestbook;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JpaConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)  // JUnit4.11 부터 지원
public class JpaGuestbookRepositoryTest {

    @Autowired
    private JpaGuestbookRepository guestbookRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void test01Save() {
        Guestbook gb1 = new Guestbook();
        gb1.setName("둘리");
        gb1.setPassword("1234");
        gb1.setContents("안녕1");
        guestbookRepository.save(gb1);

        Guestbook gb2 = new Guestbook();
        gb2.setName("마이콜");
        gb2.setPassword("1234");
        gb2.setContents("안녕2");
        guestbookRepository.save(gb2);

        Guestbook gb3 = new Guestbook();
        gb3.setName("마이콜");
        gb3.setPassword("1234");
        gb3.setContents("안녕2");
        guestbookRepository.save(gb3);

        assertEquals(3L, guestbookRepository.count());
    }

    @Test
    public void test02FindAll() {
        List<Guestbook> list = guestbookRepository.findAll();
        assertEquals(3, list.size());
    }

    @Test
    public void test03FindAll() {
        List<Guestbook> list = guestbookRepository.findAll(new Sort(Sort.Direction.ASC, "regDate"));
        assertEquals(3, list.size());
    }

    @Test
    public void test04FindAll() {
        Page<Guestbook> page = guestbookRepository.findAll(PageRequest.of(0, 2, Sort.Direction.DESC, "regDate"));
        List<Guestbook> list = page.getContent();
        assertEquals(2, list.size());
    }

    @Test
    public void test05FindAllByOrderByRegDateDesc() {
        List<Guestbook> list = guestbookRepository.findAllByOrderByRegDateDesc();
        assertEquals(3, list.size());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test06FindByIdAndDelete(){
        Guestbook gb = guestbookRepository.findById(1L).orElse(null);
        assertNotNull(gb);

        guestbookRepository.delete(gb);
        assertEquals(2L, guestbookRepository.count());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test07DeleteById(){
        guestbookRepository.deleteById(2L);
        assertEquals(1L, guestbookRepository.count());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test08DeleteByNoAndPassword() {
        assertTrue(guestbookRepository.deleteByNoAndPassword(3L, "1234") == 1);
        assertEquals(0L, guestbookRepository.count());
    }
}
