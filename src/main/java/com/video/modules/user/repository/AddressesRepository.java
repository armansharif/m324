package com.video.modules.user.repository;

import com.video.modules.user.model.Addresses;
import com.video.modules.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressesRepository extends JpaRepository<Addresses ,Long> {

    public List<Addresses> findByUsers(Users users);
}
