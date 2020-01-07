package me.kickscar.practices.jpa03.model02.repository;

import me.kickscar.practices.jpa03.model02.config.JpqlConfig;
import me.kickscar.practices.jpa03.model02.domain.GenderType;
import me.kickscar.practices.jpa03.model02.domain.RoleType;
import me.kickscar.practices.jpa03.model02.domain.User;
import me.kickscar.practices.jpa03.model02.dto.UserDto;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JpqlConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JpqlUserRepositoryTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpqlUserRepository userRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void test01Save(){
        User user = new User();
        user.setName("둘리");
        user.setPassword("1");
        user.setEmail("dooly@kickscar.me");
        user.setGender(GenderType.MALE);
        user.setRole(RoleType.USER);
        userRepository.save(user);

        assertEquals( 1L,  userRepository.count().longValue() );
    }

    @Test
    public void test02FindById(){
        User user = userRepository.findById(1L);
        assertEquals("둘리", user.getName());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test03UpdatePersisted(){
        User user = new User();
        user.setNo(1L);
        user.setName("둘리2");
        user.setPassword("2");
        user.setEmail("dooly2@kickscar.me");
        user.setGender(GenderType.MALE);
        user.setRole(RoleType.USER);

        User userPersist = userRepository.update1(user);
        assertTrue(em.contains(userPersist));
    }

    @Test
    public void test04FindByEmailAndPassword(){
        UserDto userDto = userRepository.findByEmailAndPassword("dooly2@kickscar.me", "2");
        assertEquals(1L, userDto.getNo().longValue());
        assertEquals("둘리2", userDto.getName());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test05Update2(){
        User user = new User();
        user.setNo(1L);
        user.setName("둘리3");
        user.setPassword("3");
        user.setEmail("dooly3@kickscar.me");
        user.setGender(GenderType.MALE);
        user.setRole(RoleType.USER);

        assertTrue(userRepository.update2(user));
    }

}
