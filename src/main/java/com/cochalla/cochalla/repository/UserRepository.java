package com.cochalla.cochalla.repository;

import com.cochalla.cochalla.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT u FROM User u WHERE u.resTime = :hour AND (u.lastSummaryDate < :today OR u.lastSummaryDate IS NULL)")
    List<User> findTargetUsersForSummary(@Param("hour") int hour, @Param("today") LocalDate today);
}
