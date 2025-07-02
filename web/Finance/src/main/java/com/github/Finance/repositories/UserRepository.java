package com.github.Finance.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.github.Finance.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    public Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role.id = 2")
    public List<User> findAllRegularUsers();

}
