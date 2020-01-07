package me.kickscar.practices.jpa03.model02.repository;

import me.kickscar.practices.jpa03.model02.domain.User;
import me.kickscar.practices.jpa03.model02.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Repository
public class JpqlUserRepository {

    @Autowired
    private EntityManager em;

    // 저장: 영속화
    public void save(User user){
        em.persist(user);
    }

    // 조회1: Fetch One: 영속객체 반환
    public User findById(Long no) {
        return em.find(User.class, no);
    }

    // 조회2: Fetch One: 영속객체 반환
    public UserDto findByEmailAndPassword(String email, String password) {
        String qlString = "select new me.kickscar.practices.jpa03.model02.dto.UserDto(u.no, u.name) from User u where u.email=:email and u.password=:password";
        TypedQuery<UserDto> query = em.createQuery(qlString, UserDto.class);
        query.setParameter("email", email);
        query.setParameter("password", password);

        return query.getSingleResult();
    }

    // 수정1
    public User update1(User user){
        User userPersisted = em.find(User.class, user.getNo());

        if(userPersisted != null){
            userPersisted.setRole(user.getRole());
            userPersisted.setGender(user.getGender());
            userPersisted.setEmail(user.getEmail());
            userPersisted.setName(user.getName());
            userPersisted.setPassword(user.getPassword());
        }

        return userPersisted;
    }

    // 수정2
    public Boolean update2(User user){
        Query query = em.createQuery("update User u set u.role=:role, u.gender=:gender, u.email=:email, u.name=:name, u.password=:password where u.no=:no");

        query.setParameter("no", user.getNo());
        query.setParameter("role", user.getRole());
        query.setParameter("gender", user.getGender());
        query.setParameter("email", user.getEmail());
        query.setParameter("name", user.getName());
        query.setParameter("password", user.getPassword());

        return query.executeUpdate() == 1;
    }

    // count
    public Long count() {
        String qlString = "select count(u) from User u";
        TypedQuery<Long> query = em.createQuery(qlString, Long.class);
        return query.getSingleResult();
    }
}
