package me.kickscar.practices.jpa03.model08.repository;

import me.kickscar.practices.jpa03.model08.config.JpaConfig;
import me.kickscar.practices.jpa03.model08.domain.Blog;
import me.kickscar.practices.jpa03.model08.domain.User;
import me.kickscar.practices.jpa03.model08.dto.BlogDto;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static me.kickscar.practices.jpa03.model08.domain.specs.BlogSpecs.withNameContains;
import static me.kickscar.practices.jpa03.model08.domain.specs.BlogSpecs.withUserNameContains;

import static org.junit.Assert.*;
import static org.springframework.data.jpa.domain.Specification.where;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JpaConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JpaBlogRepositoryTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaBlogRepository blogRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void test01SaveBlogs(){
        User user1 = new User();
        user1.setId("dooly");
        user1.setName("둘리");
        user1.setPassword("1234");
        User user1Persisted = userRepository.save(user1);

        Blog blog1 = new Blog();
        blog1.setName("둘리의블로그");
        blog1.setUser(user1Persisted);
        blogRepository.save(blog1);

        // =============================================

        User user2 = new User();
        user2.setId("ddochi");
        user2.setName("또치");
        user2.setPassword("1234");
        User user2Persisted = userRepository.save(user2);

        Blog blog2 = new Blog();
        blog2.setName("또치의블로그");
        blog2.setUser(user2Persisted);
        blogRepository.save(blog2);

        // =============================================

        User user3 = new User();
        user3.setId("donut");
        user3.setName("도우넛");
        user3.setPassword("1234");
        User user3Persisted = userRepository.save(user3);

        Blog blog3 = new Blog();
        blog3.setName("도넛의블로그");
        blog3.setUser(user3Persisted);
        blogRepository.save(blog3);

        // =============================================

        User user4 = new User();
        user4.setId("michol");
        user4.setName("마이콜");
        user4.setPassword("1234");
        User user4Persisted = userRepository.save(user4);

        Blog blog4 = new Blog();
        blog4.setName("마이콜의블로그");
        blog4.setUser(user4Persisted);
        blogRepository.save(blog4);

        // =============================================

        User user5 = new User();
        user5.setId("younghee");
        user5.setName("영희");
        user5.setPassword("1234");
        User user5Persisted = userRepository.save(user5);

        Blog blog5 = new Blog();
        blog5.setName("영희의블로그");
        blog5.setUser(user5Persisted);
        blogRepository.save(blog5);

        // =============================================

        User user6 = new User();
        user6.setId("gildong");
        user6.setName("길동");
        user6.setPassword("1234");
        User user6Persisted = userRepository.save(user6);

        Blog blog6 = new Blog();
        blog6.setName("길동의블로그");
        blog6.setUser(user6Persisted);
        blogRepository.save(blog6);

        // =============================================

        User user7 = new User();
        user7.setId("heedong");
        user7.setName("희동");
        user7.setPassword("1234");
        User user7Persisted = userRepository.save(user7);

        Blog blog7 = new Blog();
        blog7.setName("희동의블로그");
        blog7.setUser(user7Persisted);
        blogRepository.save(blog7);

        assertEquals(7L, userRepository.count());
        assertEquals(7L, blogRepository.count());
    }

    @Test
    public void test02findAll2(){
        Integer page = 0;
        final Integer size = 3;

        Page<BlogDto> pageBlogDto = blogRepository.findAll2(Optional.of("블"), Optional.of("희"), PageRequest.of(page++, size, Sort.Direction.DESC, "openDate"));
        assertEquals(2, pageBlogDto.get().count());
    }

    @Test
    public void test03findAll3(){
        Integer page = 0;
        final Integer size = 3;

        Page<BlogDto> pageBlogDto = blogRepository.findAll3(Optional.of("블"), Optional.empty(), PageRequest.of(page++, size, Sort.Direction.DESC, "openDate"));
        assertEquals(3, pageBlogDto.get().count());
    }

    @Test
    public void test04findAll4(){
        Integer page = 0;
        final Integer size = 3;

        Page<BlogDto> pageBlogDto = blogRepository.findAll4(Optional.of("블"), Optional.of("희"), PageRequest.of(page++, size, Sort.Direction.DESC, "openDate"));
        assertEquals(2, pageBlogDto.get().count());
    }

    @Test
    public void test05findAll(){
        Integer page = 0;
        final Integer size = 3;

        Page<Blog> pageBlogDto = blogRepository.findAll(where(withNameContains("블")).and(withUserNameContains("희")), PageRequest.of(page++, size, Sort.Direction.DESC, "openDate"));
        assertEquals(2, pageBlogDto.get().count());
    }
}