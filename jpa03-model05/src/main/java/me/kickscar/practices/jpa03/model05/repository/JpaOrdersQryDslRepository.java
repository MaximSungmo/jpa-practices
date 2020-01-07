package me.kickscar.practices.jpa03.model05.repository;

import me.kickscar.practices.jpa03.model05.domain.Orders;

import java.util.List;

public interface JpaOrdersQryDslRepository {
    public void save(Long userNo, Orders ...orders);

}
