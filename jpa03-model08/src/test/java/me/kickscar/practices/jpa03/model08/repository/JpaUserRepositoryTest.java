package me.kickscar.practices.jpa03.model08.repository;

import me.kickscar.practices.jpa03.model08.config.JpaConfig;
import me.kickscar.practices.jpa03.model08.domain.Blog;
import me.kickscar.practices.jpa03.model08.domain.User;
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
@SpringBootTest(classes = {JpaConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JpaUserRepositoryTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaUserRepository userRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void test01Save() {
        User user1 = new User();
        user1.setId("dooly");
        user1.setName("둘리");
        user1.setPassword("1234");

        User user1Persisted = userRepository.save(user1);

        assertFalse(em.contains(user1));
        assertTrue(em.contains(user1Persisted));
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test02Save() {
        User user1 = new User();
        user1.setId("dooly");
        user1.setName("둘리2");
        user1.setPassword("1234");

        User user1Persisted = userRepository.save(user1);

        assertFalse(em.contains(user1));
        assertTrue(em.contains(user1Persisted));
    }
}