package me.kickscar.practices.jpa03.model06.repository;

import me.kickscar.practices.jpa03.model06.config.JpaConfig;
import me.kickscar.practices.jpa03.model06.domain.Blog;
import me.kickscar.practices.jpa03.model06.domain.User;
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

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JpaConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JpaUserRepositoryTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaBlogRepository blogRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void test01Save() {
        User user = new User();
        user.setId("dooly");
        user.setName("둘리");
        user.setPassword("1234");

        userRepository.save(user);

        assertEquals(1L, userRepository.count());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test02SaveBlog(){
        Blog blog = new Blog();
        blog.setName("둘리의블로그");
        Blog blogPersisted = blogRepository.save(blog);

        User user = userRepository.findById("dooly").get();
        user.setBlog(blogPersisted);

        assertEquals(1L, blogRepository.count());
    }

    @Test
    @Transactional
    public void test03FindById(){
        User user = userRepository.findById("dooly").get();
        assertFalse(em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(user.getBlog()));

        assertEquals("둘리의블로그", user.getBlog().getName());
        assertTrue(em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(user.getBlog()));
    }
}