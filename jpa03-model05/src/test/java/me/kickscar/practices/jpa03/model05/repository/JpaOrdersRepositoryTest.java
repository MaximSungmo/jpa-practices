package me.kickscar.practices.jpa03.model05.repository;

import me.kickscar.practices.jpa03.model05.config.JpaConfig;
import me.kickscar.practices.jpa03.model05.domain.GenderType;
import me.kickscar.practices.jpa03.model05.domain.Orders;
import me.kickscar.practices.jpa03.model05.domain.RoleType;
import me.kickscar.practices.jpa03.model05.domain.User;
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
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JpaConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JpaOrdersRepositoryTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaOrdersRepository ordersRepository;

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

        User user2 = new User();
        user2.setName("마이콜");
        user2.setPassword("1234");
        user2.setEmail("michol@kickscar.me");
        user2.setGender(GenderType.MALE);
        user2.setRole(RoleType.USER);
        userRepository.save(user2);

        ordersRepository.save(1L, new Orders("주문1"), new Orders("주문2"), new Orders("주문3"));

        assertEquals(3L, ordersRepository.count());
   }

    @Test
    @Transactional
    @Rollback(false)
    public void test02UpdateUser() {
        Orders orders = ordersRepository.findById(1L).get();

        User user = userRepository.findById(2L).get();
        orders.setUser(user);
    }

    @Test
    @Transactional
    public void test03UpdateUserResultFails() {
        Orders orders = ordersRepository.findById(1L).get();
        assertNotEquals("마이콜", orders.getUser().getName());
    }
}