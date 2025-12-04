package com.calendarapp.CalendarTaskAplication.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CollectionId;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "Team")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "description")
    private String description;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User user;

    @NonNull()
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "team_id")
    ,inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> members = new HashSet<>();
}
