package me.kickscar.practices.jpa03.model01.repository;

import me.kickscar.practices.jpa03.model01.config.JpqlConfig;
import me.kickscar.practices.jpa03.model01.domain.Guestbook;
import me.kickscar.practices.jpa03.model01.dto.GuestbookDto;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JpqlConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)  // JUnit4.11 부터 지원
@Transactional
public class JpqlGuestbookRepositoryTest {

    @Autowired
    private JpqlGuestbookRepository guestbookRepository;

    @Test
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

        assertEquals(2L, guestbookRepository.count().longValue());
    }

    @Test
    public void test02FindAll1() {
        List<Guestbook> list = guestbookRepository.findAll1();
        assertEquals(2, list.size());
    }

    @Test
    public void test03FindAll2() {
        List<GuestbookDto> list = guestbookRepository.findAll2();
        assertEquals(2, list.size());
    }

    @Test
    public void test04Delete() {
        assertTrue(guestbookRepository.delete(1L, "1234"));
    }
}
