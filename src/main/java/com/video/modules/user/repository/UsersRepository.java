package com.video.modules.user.repository;

import com.video.modules.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users,Long> {

    Users findByEmail(String email);
    Users findByMobile(String mobile);


    @Query(nativeQuery = true, value = "select * from users where mobile =:mobile and password=:code and  NOW()  <= DATE_ADD(updated_at, INTERVAL 5 MINUTE) ")
    Users validateVerificationCode(@Param("mobile") String mobile,@Param("code") String code);

    @Query("select u from Users u where u.email = :email")
    Users findByQuery(@Param("email") String email);


}
