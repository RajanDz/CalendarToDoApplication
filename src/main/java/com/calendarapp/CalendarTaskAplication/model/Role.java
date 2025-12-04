package com.calendarapp.CalendarTaskAplication.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role")
@Getter
@RequiredArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "description")
    private String description;


}
