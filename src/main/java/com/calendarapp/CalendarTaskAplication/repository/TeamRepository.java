package com.calendarapp.CalendarTaskAplication.repository;

import com.calendarapp.CalendarTaskAplication.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team,Integer> {
}
