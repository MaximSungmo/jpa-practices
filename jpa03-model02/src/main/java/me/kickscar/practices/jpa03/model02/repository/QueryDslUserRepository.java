package me.kickscar.practices.jpa03.model02.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import me.kickscar.practices.jpa03.model02.domain.User;
import me.kickscar.practices.jpa03.model02.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static me.kickscar.practices.jpa03.model02.domain.QUser.user;

@Repository
public class QueryDslUserRepository extends QuerydslRepositorySupport {

    @Autowired
    private JPAQueryFactory queryFactory;

    public QueryDslUserRepository() {
        super(User.class);
    }

    // 저장: 영속화
    public void save(User user) {
        getEntityManager().persist(user);
    }

    // 조회1: Fetch One: 영속객체 반환
    public User findById(Long no) {
        return getEntityManager().find(User.class, no);
    }

    // 조회2: Fetch One: 영속객체 반환
    public User findById2(Long no) {
        return (User) queryFactory
                .from(user)
                .where(user.no.eq(no))
                .fetchOne();
    }

    // 조회3: Fetch One: Projection
    public UserDto findByEmailAndPassword(String email, String password) {
        return queryFactory
                .select(Projections.constructor(UserDto.class, user.no, user.name))
                .from(user)
//               다음 2개의 where 메소드는 완전 동일
//              .where(user.email.eq(email).and(user.password.eq(password)))
                .where(user.email.eq(email), user.password.eq(password))
                .fetchOne();
    }

    // 수정1
    public User update1(User user){
        User userPersisted = getEntityManager().find(User.class, user.getNo());

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
    public Boolean update2(User argUser) {
        return queryFactory.update(user)
                .where(user.no.eq(argUser.getNo()))
                .set(user.name, argUser.getName())
                .set(user.email, argUser.getEmail())
                .set(user.password, argUser.getPassword())
                .set(user.gender, argUser.getGender())
                .set(user.role, argUser.getRole())
                .execute() == 1L;
    }

    // count
    public Long count() {
        return queryFactory
                .from(user)
                .fetchCount();
    }

}
