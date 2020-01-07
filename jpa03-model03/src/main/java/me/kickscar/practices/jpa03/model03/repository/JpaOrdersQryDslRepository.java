package me.kickscar.practices.jpa03.model03.repository;

import me.kickscar.practices.jpa03.model03.domain.Orders;
import me.kickscar.practices.jpa03.model03.dto.OrderCountOfUserDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface JpaOrdersQryDslRepository {
    List<Orders> findAllByUserNo2(Long userNo);
    List<Orders> findAllByUserNo2(Long userNo, Sort sort);
    List<Orders> findAllByUserNo2(Long userNo, Pageable pageable);

    List<OrderCountOfUserDto> countAllGroupByUser();
}
