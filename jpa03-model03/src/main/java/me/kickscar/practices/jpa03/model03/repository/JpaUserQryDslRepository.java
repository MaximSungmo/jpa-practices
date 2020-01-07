package me.kickscar.practices.jpa03.model03.repository;

import me.kickscar.practices.jpa03.model03.domain.Orders;
import me.kickscar.practices.jpa03.model03.domain.User;
import me.kickscar.practices.jpa03.model03.dto.UserDto;

import java.util.List;

public interface JpaUserQryDslRepository {
    public UserDto findById2(Long no);
    public Boolean update(User user);

    public List<User> findAllCollectionJoinProblem();
    public List<User> findAllCollectionJoinProblemSolved();
    public List<User> findAllCollectionJoinAndNplusOneProblemSolved();

    public List<Orders> findOrdersByNo(Long no);
}
