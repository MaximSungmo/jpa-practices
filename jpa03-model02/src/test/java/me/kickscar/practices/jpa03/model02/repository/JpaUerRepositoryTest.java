package me.kickscar.practices.jpa03.model02.repository;

import me.kickscar.practices.jpa03.model02.config.JpaConfig;
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

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JpaConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JpaUerRepositoryTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaUserRepository userRepository;

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

        assertEquals( 1L,  userRepository.count());
    }

    @Test
    public void test02FindById(){
        User user = userRepository.findById(1L).get();
        assertEquals("둘리", user.getName());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test03Update(){
        User user = userRepository.findById(1L).get();

        user.setName("둘리2");
        user.setPassword("2");
        user.setEmail("dooly2@kickscar.me");
        user.setGender(GenderType.MALE);
        user.setRole(RoleType.USER);
    }

    @Test
    public void test04FindById2(){
        UserDto userDto = userRepository.findById2(1L);
        assertEquals("둘리2", userDto.getName());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test05Update() {
        userRepository.update(1L, "둘리3", "dooly3@kickscar.me", "3", GenderType.MALE, RoleType.USER);
    }

    @Test
    public void test06FindByEmailAndPassword(){
        User user = userRepository.findByEmailAndPassword("dooly3@kickscar.me", "3");
        assertEquals("둘리3", user.getName());
    }
}