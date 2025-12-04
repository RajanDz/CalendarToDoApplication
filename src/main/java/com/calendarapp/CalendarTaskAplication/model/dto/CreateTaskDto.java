package com.calendarapp.CalendarTaskAplication.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class CreateTaskDto {


    private String name;
    private String description;
    private LocalDate dateOfStart;
    private LocalTime timeOfStart;
    private LocalDate dateOfEnd;
    private LocalTime timeOfEnd;
    private String status;
}
