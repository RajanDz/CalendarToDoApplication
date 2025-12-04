package com.calendarapp.CalendarTaskAplication.repository;

import com.calendarapp.CalendarTaskAplication.model.Task;
import com.calendarapp.CalendarTaskAplication.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Integer> {


    @Query(value = """
            select t.* from task t
            join user_tasks ut on ut.task_id = t.id
            where ut.user_id = :userId
            and t.date_of_start = :dateToday
            and t.time_of_start > :timeNow
            """, nativeQuery = true)
    List<Task> findTaskForToday(int userId, LocalDate dateToday, LocalTime timeNow);

    @Query(value = """
            select t.* from task t
            join user_tasks ut on ut.task_id = t.id
            where ut.user_id = :userId
            and t.date_of_start = :dateOfStart
            """, nativeQuery = true)
    List<Task> findTasksByDate(int userId, LocalDate dateOfStart);

    @Query(value = """
            select t.* from task t
            join user_tasks ut on ut.task_id = t.id
            where ut.user_id = :userId
            and t.date_of_start between :startDate and :endDate
            """, nativeQuery = true)
    List<Task> findTaskForNext7Days(@Param("userId") int userId,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);

}
