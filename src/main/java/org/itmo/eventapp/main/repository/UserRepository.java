package org.itmo.eventapp.main.repository;

import org.itmo.eventapp.main.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserLoginInfo_Login(String login);

    boolean existsByRoleId(Integer roleId);

    @Query("SELECT u FROM User u " +
            "WHERE LOWER(u.name) LIKE LOWER(concat('%', :rightPart,'%')) " +
            "OR LOWER(u.name) LIKE LOWER(concat('%', :leftPart,'%')) " +
            "OR LOWER(u.surname) LIKE LOWER(concat('%', :rightPart,'%')) " +
            "OR LOWER(u.surname) LIKE LOWER(concat('%', :leftPart,'%'))")
    Page<User> findByFullName(
            @Param("rightPart") String rightPart,
            @Param("leftPart") String leftPart,
            Pageable pageable);
}
