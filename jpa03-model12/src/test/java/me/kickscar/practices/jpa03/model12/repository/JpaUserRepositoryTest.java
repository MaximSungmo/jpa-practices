package me.kickscar.practices.jpa03.model12.repository;

import me.kickscar.practices.jpa03.model12.config.JpaConfig;
import me.kickscar.practices.jpa03.model12.domain.Book;
import me.kickscar.practices.jpa03.model12.domain.CartItem;
import me.kickscar.practices.jpa03.model12.domain.User;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JpaConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JpaUserRepositoryTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaBookRepository bookRepository;

    @Autowired
    private JpaCartItemRepository cartItemRepository;

    @Test
    @Transactional
    public void test02FindById() {
        User user = userRepository.findById(1L).get();
        List<CartItem> cart = user.getCart();

        assertEquals(2, cart.size());
        assertEquals("둘리", cart.get(0).getUser().getName());
        assertEquals("책2", cart.get(1).getBook().getTitle());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test01Save() {
        User user1 = new User();
        user1.setName("둘리");
        user1.setEmail("dooly@gmail.com");
        user1.setPassword("1234");
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("마이콜");
        user2.setEmail("michol@gmail.com");
        user2.setPassword("1234");
        userRepository.save(user2);

        Book book1 = new Book();
        book1.setTitle("책1");
        book1.setPrice(1000);
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setTitle("책2");
        book2.setPrice(1000);
        bookRepository.save(book2);

        Book book3 = new Book();
        book3.setTitle("책3");
        book3.setPrice(1000);
        bookRepository.save(book3);

        CartItem cartItem1 = new CartItem();
        cartItem1.setUser(user1);
        cartItem1.setBook(book1);
        cartItem1.setAmount(1);
        cartItemRepository.save(cartItem1);

        CartItem cartItem2 = new CartItem();
        cartItem2.setUser(user1);
        cartItem2.setBook(book2);
        cartItem2.setAmount(2);
        cartItemRepository.save(cartItem2);

        CartItem cartItem3 = new CartItem();
        cartItem3.setUser(user2);
        cartItem3.setBook(book1);
        cartItem3.setAmount(1);
        cartItemRepository.save(cartItem3);

        CartItem cartItem4 = new CartItem();
        cartItem4.setUser(user2);
        cartItem4.setBook(book2);
        cartItem4.setAmount(1);
        cartItemRepository.save(cartItem4);

        CartItem cartItem5 = new CartItem();
        cartItem5.setUser(user2);
        cartItem5.setBook(book3);
        cartItem5.setAmount(1);
        cartItemRepository.save(cartItem5);

        assertEquals(2, userRepository.count());
        assertEquals(3, bookRepository.count());
        assertEquals(5, cartItemRepository.count());
    }
}