package me.kickscar.practices.jpa03.model02.repository;

import me.kickscar.practices.jpa03.model02.domain.GenderType;
import me.kickscar.practices.jpa03.model02.domain.RoleType;
import me.kickscar.practices.jpa03.model02.domain.User;
import me.kickscar.practices.jpa03.model02.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaUserRepository extends JpaRepository<User, Long> {

    public User findByEmailAndPassword(String email, String password);

    @Query("select new me.kickscar.practices.jpa03.model02.dto.UserDto(u.no, u.name) from User u where u.no=:no")
    public UserDto findById2(@Param("no") Long no);

    @Modifying
    @Query("update User u set u.name=:name, u.email=:email, u.password=:password, u.gender=:gender, u.role=:role where u.no=:no")
    public void update(
            @Param("no") Long no,
            @Param("name") String name,
            @Param("email") String email,
            @Param("password") String password,
            @Param("gender") GenderType gender,
            @Param("role") RoleType role);
}
