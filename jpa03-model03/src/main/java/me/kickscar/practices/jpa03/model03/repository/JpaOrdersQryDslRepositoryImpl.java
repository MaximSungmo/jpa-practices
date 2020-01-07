package me.kickscar.practices.jpa03.model03.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import me.kickscar.practices.jpa03.model03.domain.Orders;
import me.kickscar.practices.jpa03.model03.dto.OrderCountOfUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static me.kickscar.practices.jpa03.model03.domain.QOrders.orders;

public class JpaOrdersQryDslRepositoryImpl extends QuerydslRepositorySupport implements JpaOrdersQryDslRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    public JpaOrdersQryDslRepositoryImpl() {
        super(Orders.class);
    }

    @Override
    public List<Orders> findAllByUserNo2(Long userNo) {
        return (List<Orders>) queryFactory
                .select(orders)
                .from(orders)
                .innerJoin(orders.user)
                .fetchJoin()
                .where(orders.user.no.eq(userNo))
                .fetch();
    }

    @Override
    public List<Orders> findAllByUserNo2(Long userNo, Sort sort) {
        JPAQuery<Orders> query = queryFactory
                .select(orders)
                .from(orders)
                .innerJoin(orders.user)
                .fetchJoin()
                .where(orders.user.no.eq(userNo));

        for (Sort.Order o : sort) {
            PathBuilder orderByExpression = new PathBuilder(Orders.class, "orders");
            query.orderBy(new OrderSpecifier(o.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC, orderByExpression.get(o.getProperty())));
        }

        return query.fetch();
    }

    @Override
    public List<Orders> findAllByUserNo2(Long userNo, Pageable pageable) {
        JPAQuery<Orders> query = queryFactory
                .select(orders)
                .from(orders)
                .innerJoin(orders.user)
                .fetchJoin()
                .where(orders.user.no.eq(userNo));

        if (pageable != null) {

            query.offset(pageable.getOffset());
            query.limit(pageable.getPageSize());

            for (Sort.Order o : pageable.getSort()) {
                PathBuilder orderByExpression = new PathBuilder(Orders.class, "orders");
                query.orderBy(new OrderSpecifier(o.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC, orderByExpression.get(o.getProperty())));
            }
        }

        return query.fetch();
    }

    @Override
    public List<OrderCountOfUserDto> countAllGroupByUser(){
        return queryFactory
                .select(Projections.constructor(OrderCountOfUserDto.class, orders.user.no, orders.user.no.count()))
                .from(orders)
                .innerJoin(orders.user)
                .groupBy(orders.user)
                .fetch();
    }
}
