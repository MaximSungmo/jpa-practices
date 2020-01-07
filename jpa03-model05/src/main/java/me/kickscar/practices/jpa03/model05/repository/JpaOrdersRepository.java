package me.kickscar.practices.jpa03.model05.repository;

import me.kickscar.practices.jpa03.model05.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrdersRepository extends JpaRepository<Orders, Long>, JpaOrdersQryDslRepository {
}
