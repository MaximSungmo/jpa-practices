package me.kickscar.practices.jpa03.model03.repository;

import me.kickscar.practices.jpa03.model03.domain.Orders;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaOrdersRepository extends JpaRepository<Orders, Long>, JpaOrdersQryDslRepository {
	List<Orders> findAllByUserNo(Long userNo);
	List<Orders> findAllByUserNo(Long userNo, Sort sort);
	Long countAllByUserNo(Long userNo);
}
