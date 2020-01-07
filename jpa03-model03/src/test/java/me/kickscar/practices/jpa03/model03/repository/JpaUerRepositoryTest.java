package me.kickscar.practices.jpa03.model03.repository;

import me.kickscar.practices.jpa03.model03.config.JpaConfig;
import me.kickscar.practices.jpa03.model03.domain.GenderType;
import me.kickscar.practices.jpa03.model03.domain.Orders;
import me.kickscar.practices.jpa03.model03.domain.RoleType;
import me.kickscar.practices.jpa03.model03.domain.User;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JpaConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JpaUerRepositoryTest {

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
        User user1Persisted = userRepository.save(user1);

        Orders orders1 = new Orders();
        orders1.setName("주문1");
        orders1.setUser(user1Persisted);
        ordersRepository.save(orders1);

        Orders orders2 = new Orders();
        orders2.setName("주문2");
        orders2.setUser(user1Persisted);
        ordersRepository.save(orders2);

        Orders orders3 = new Orders();
        orders3.setName("주문3");
        orders3.setUser(user1Persisted);
        ordersRepository.save(orders3);

        Orders orders4 = new Orders();
        orders4.setName("주문4");
        orders4.setUser(user1Persisted);
        ordersRepository.save(orders4);

        Orders orders5 = new Orders();
        orders5.setName("주문5");
        orders5.setUser(user1Persisted);
        ordersRepository.save(orders5);

        //================================

        User user2 = new User();
        user2.setName("마이콜");
        user2.setPassword("1234");
        user2.setEmail("michol@kickscar.me");
        user2.setGender(GenderType.MALE);
        user2.setRole(RoleType.USER);
        User user2Persisted = userRepository.save(user2);

        Orders orders6 = new Orders();
        orders6.setName("주문6");
        orders6.setUser(user2Persisted);
        ordersRepository.save(orders6);

        Orders orders7 = new Orders();
        orders7.setName("주문7");
        orders7.setUser(user2Persisted);
        ordersRepository.save(orders7);

        //================================

        User user3 = new User();
        user3.setName("또치");
        user3.setPassword("1234");
        user3.setEmail("ddochi@kickscar.me");
        user3.setGender(GenderType.MALE);
        user3.setRole(RoleType.USER);
        User user3Persisted = userRepository.save(user3);

        Orders orders8 = new Orders();
        orders8.setName("주문8");
        orders8.setUser(user3Persisted);
        ordersRepository.save(orders8);

        assertEquals(8L, ordersRepository.count());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test02Update() {
        User user = new User();
        user.setNo(1L);
        user.setEmail("dooly3@kickscar.me");
        user.setName("둘리3");
        user.setPassword("3");
        user.setGender(GenderType.MALE);
        user.setRole(RoleType.USER);

        assertTrue(userRepository.update(user));
    }

    @Test
    @Transactional
    public void test03OneToManyCollectionJoinProblem() {
        assertEquals(ordersRepository.count(), userRepository.findAllCollectionJoinProblem().size());
    }

    @Test
    @Transactional
    public void test04OCollectionJoinProblemSolved() {
        assertEquals(userRepository.count(), userRepository.findAllCollectionJoinProblemSolved().size());
    }

    @Test
    @Transactional
    public void test05NplusOneProblem() {
        Integer qryCount = 0;
        Long ordersCountActual = 0L;

        Long ordersCountExpected = ordersRepository.count();
        Long N = userRepository.count();

        qryCount++;
        List<User> users = userRepository.findAll();

        for(User user : users) {
            List<Orders> result = user.getOrders();

            if(!em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(result)){
                qryCount++;
            }
            ordersCountActual += result.size();
        }

        assertEquals(ordersCountExpected, ordersCountActual);
        assertEquals(N+1, qryCount.longValue());
    }

    @Test
    @Transactional
    public void test06NplusOneProblemNotSolvedYet() {
        Integer qryCount = 0;
        Long ordersCountActual = 0L;

        Long ordersCountExpected = ordersRepository.count();
        Long N = userRepository.count();

        qryCount++;
        List<User> users = userRepository.findAllCollectionJoinProblemSolved();

        for(User user : users) {
            List<Orders> result = user.getOrders();

            if(!em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(result)){
                qryCount++;
            }
            ordersCountActual += result.size();
        }

        assertEquals(ordersCountExpected, ordersCountActual);
        assertEquals(N+1, qryCount.longValue());
    }

    @Test
    public void test07NplusOneProblemSolved() {
        Integer qryCount = 0;
        Long ordersCountActual = 0L;

        Long ordersCountExpected = ordersRepository.count();
        Long N = userRepository.count();

        qryCount++;
        List<User> users = userRepository.findAllCollectionJoinAndNplusOneProblemSolved();

        for(User user : users) {
            List<Orders> result = user.getOrders();

            if(!em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(result)){
                qryCount++;
            }
            ordersCountActual += result.size();
        }

        assertEquals(ordersCountExpected, ordersCountActual);
        assertEquals(1, qryCount.longValue());
    }

    @Test
    public void test08findOrdersByNo() {
        assertEquals(5, userRepository.findOrdersByNo(1L).size());

    }
}