package com.calendarapp.CalendarTaskAplication.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "task")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@ToString
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "description")
    private String description;

    @NonNull
    @Column(name = "date_of_start")
    private LocalDate dateOfStart;


    @NonNull
    @Column(name = "time_of_start")
    private LocalTime timeOfStart;


    @NonNull
    @Column(name = "date_of_end")
    private LocalDate dateOfEnd;


    @NonNull
    @Column(name = "time_of_end")
    private LocalTime timeOfEnd;

    @NonNull
    @Column(name = "status")
    private String status;

}
