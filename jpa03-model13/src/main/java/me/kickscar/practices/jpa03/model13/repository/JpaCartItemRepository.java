package me.kickscar.practices.jpa03.model13.repository;

import me.kickscar.practices.jpa03.model13.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaCartItemRepository extends JpaRepository<CartItem, Long>, JpaCartItemQryDslRepository {
    List<CartItem> findAllByUserNo(Long userNo);
    void deleteByUserNoAndBookNo(Long userNo, Long bookNo);
}
