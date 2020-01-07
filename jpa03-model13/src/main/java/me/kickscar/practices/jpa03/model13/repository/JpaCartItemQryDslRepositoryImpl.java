package me.kickscar.practices.jpa03.model13.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import me.kickscar.practices.jpa03.model13.domain.CartItem;
import me.kickscar.practices.jpa03.model13.dto.CartItemDto;
import me.kickscar.practices.jpa03.model13.dto.QCartItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

import static me.kickscar.practices.jpa03.model13.domain.QCartItem.cartItem;

public class JpaCartItemQryDslRepositoryImpl extends QuerydslRepositorySupport implements JpaCartItemQryDslRepository {

    @Autowired
    private JPAQueryFactory queryFactory;

    public JpaCartItemQryDslRepositoryImpl() {
        super(CartItem.class);
    }

    @Override
    public List<CartItem> findAllByUserNo2(Long userNo) {
        return  queryFactory
                .select(cartItem)
                .from(cartItem)
                .innerJoin(cartItem.user)
                .fetchJoin()
                .innerJoin(cartItem.book)
                .fetchJoin()
                .where(cartItem.user.no.eq(userNo))
                .fetch();
    }

    @Override
    public List<CartItemDto> findAllByUserNo3(Long userNo) {
        return  queryFactory
                .select(new QCartItemDto(cartItem.book.no, cartItem.book.title, cartItem.book.price, cartItem.amount))
                .from(cartItem)
                .innerJoin(cartItem.user)
                .innerJoin(cartItem.book)
                .where(cartItem.user.no.eq(userNo))
                .fetch();
    }

    @Override
    public void deleteByUserNoAndBookNo2(Long userNo, Long bookNo) {
        queryFactory
                .delete(cartItem)
                .where(cartItem.user.no.eq(userNo).and(cartItem.book.no.eq(bookNo)))
                .execute();
    }
}
