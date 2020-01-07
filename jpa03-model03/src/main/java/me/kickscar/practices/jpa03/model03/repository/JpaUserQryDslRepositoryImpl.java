package me.kickscar.practices.jpa03.model03.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import me.kickscar.practices.jpa03.model03.domain.Orders;
import me.kickscar.practices.jpa03.model03.domain.User;
import me.kickscar.practices.jpa03.model03.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static me.kickscar.practices.jpa03.model03.domain.QUser.user;

public class JpaUserQryDslRepositoryImpl extends QuerydslRepositorySupport implements JpaUserQryDslRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    public JpaUserQryDslRepositoryImpl() {
        super(User.class);
    }

    @Override
    public UserDto findById2(Long no){
        return queryFactory
                .select(Projections.bean(UserDto.class, user.no, user.name))
                .from(user)
                .where(user.no.eq(no))
                .fetchOne();
    }

    @Override
    public Boolean update(User argUser) {
        return queryFactory
                .update(user)
                .where(user.no.eq(argUser.getNo()))
                .set(user.name, argUser.getName())
                .set(user.email, argUser.getEmail())
                .set(user.password, argUser.getPassword())
                .set(user.gender, argUser.getGender())
                .set(user.role, argUser.getRole())
                .execute() == 1L;
    }

    @Override
    public List<User> findAllCollectionJoinProblem(){
        return (List<User>)queryFactory
                .select(user)
                .from(user)
                .innerJoin(user.orders)
                .fetch();
    }

    public List<User> findAllCollectionJoinProblemSolved(){
        return (List<User>)queryFactory
                .selectDistinct(user)
                .from(user)
                .innerJoin(user.orders)
                .fetch();
    }

    public List<User> findAllCollectionJoinAndNplusOneProblemSolved(){
        return (List<User>)queryFactory
                .selectDistinct(user)
                .from(user)
                .innerJoin(user.orders)
                .fetchJoin()
                .fetch();
    }

    @Override
    public List<Orders> findOrdersByNo(Long no) {
        return queryFactory
                .selectDistinct(user)
                .from(user)
                .innerJoin(user.orders)
                .fetchJoin()
                .where(user.no.eq(no))
                .fetchOne()
                .getOrders();
    }
}
