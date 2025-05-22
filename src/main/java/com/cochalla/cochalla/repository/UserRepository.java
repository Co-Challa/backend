package com.cochalla.cochalla.repository;

import com.cochalla.cochalla.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserId(String userId);

    boolean existsByUserId(String userId);

    boolean existsByNickname(String nickname);

    @Query("SELECT u FROM User u WHERE u.resTime = :hour AND (u.lastSummaryDate < :today OR u.lastSummaryDate IS NULL)")
    List<User> findTargetUsersForSummary(@Param("hour") int hour, @Param("today") LocalDate today);
}