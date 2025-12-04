package com.calendarapp.CalendarTaskAplication.repository;

import com.calendarapp.CalendarTaskAplication.model.Task;
import com.calendarapp.CalendarTaskAplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String username);

    @Query(value = """
    SELECT t.*
    FROM task t
    JOIN user_tasks ut ON ut.task_id = t.id
    WHERE ut.user_id = :userId
""", nativeQuery = true)
    List<Task> findByUserId(@Param("userId") int userId);

    Optional<User> findByEmail(String email);
}
