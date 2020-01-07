package me.kickscar.practices.jpa03.model12.repository;

import me.kickscar.practices.jpa03.model12.domain.CartItem;
import me.kickscar.practices.jpa03.model12.domain.CartItemId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaCartItemRepository extends JpaRepository<CartItem, CartItemId>, JpaCartItemQryDslRepository {
    List<CartItem> findAllByUserNo(Long userNo);
    void deleteByUserNoAndBookNo(Long userNo, Long bookNo);
}
