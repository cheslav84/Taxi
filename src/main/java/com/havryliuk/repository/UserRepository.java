package com.havryliuk.repository;

import com.havryliuk.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    Optional<User> findByEmail(String email);

//    @Modifying
//    @Query("update User u set u.balance = ?1, where u.id = ?2")
//    void setUserBalanceById(BigDecimal balance, String id);

}
