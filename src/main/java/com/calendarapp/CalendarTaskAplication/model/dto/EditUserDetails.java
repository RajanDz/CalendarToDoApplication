package com.calendarapp.CalendarTaskAplication.model.dto;

import org.springframework.web.multipart.MultipartFile;

public record EditUserDetails(
        String firstname,
        String lastname,
        String username,
        MultipartFile profilePicture) {
}
