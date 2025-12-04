package com.calendarapp.CalendarTaskAplication.model.dto;


import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class EditTaskDetailsDto {
    private int taskId;
    private String name;
    private String description;
    private LocalDate dateOfStart;
    private LocalTime timeOfStart;
    private LocalDate dateOfEnd;
    private LocalTime timeOfEnd;
    private String status;
}
