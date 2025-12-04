package com.calendarapp.CalendarTaskAplication.model.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class RegistrationDto {
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String password;
    private MultipartFile profilePicture;
}
