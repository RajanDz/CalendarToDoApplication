package com.calendarapp.CalendarTaskAplication.controller;


import com.calendarapp.CalendarTaskAplication.model.Task;
import com.calendarapp.CalendarTaskAplication.model.User;
import com.calendarapp.CalendarTaskAplication.model.dto.CreateTaskDto;
import com.calendarapp.CalendarTaskAplication.model.dto.EditTaskDetailsDto;
import com.calendarapp.CalendarTaskAplication.security.jwt.JwtUtills;
import com.calendarapp.CalendarTaskAplication.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final JwtUtills jwtUtills;
    public UserController(UserService userService, JwtUtills jwtUtills) {
        this.userService = userService;
        this.jwtUtills = jwtUtills;
    }


    @GetMapping("/fetchTasks/{userId}")
    public ResponseEntity<List<Task>> fetchUserTasks(@PathVariable(name = "userId") int userId){
        List<Task> fetchTasks = userService.findUsersTask(userId);
        return ResponseEntity.ok().body(fetchTasks);
    }

    @GetMapping("/findByUsername")
    public ResponseEntity<User> findByUsername(HttpServletRequest request){
            User findUserByUsername = userService.findUserByUsername(request);
            return ResponseEntity.ok().body(findUserByUsername);
    }

    @GetMapping("/upcomingTask")
    public ResponseEntity<Task> getUpcomingTask(HttpServletRequest request){
        Task findTask = userService.findUpcomingTask(request);
        if (findTask == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(findTask);
    }

    @GetMapping("/getTasksByDate/{date}")
    public ResponseEntity<List<Task>> findTaskByDate(HttpServletRequest request, @PathVariable(name = "date")LocalDate date){
        List<Task> findTasksByDate = userService.findTasksByDate(request,date);
        return ResponseEntity.ok().body(findTasksByDate);
    }

    @GetMapping("/userProfileImg")
    public ResponseEntity<String> getUserProfileImg(HttpServletRequest request){
        String userImg = userService.getUserProfileImage(request);
        return ResponseEntity.ok().body(userImg);
    }

    @GetMapping("/getTaskForNext7Days")
    public ResponseEntity<List<Task>> getTasksForNext7Days(HttpServletRequest request){
        List<Task> getTasks = userService.getTasksForNext7Days(request);
        return ResponseEntity.ok().body(getTasks);
    }
    @PostMapping("/createTask")
    public ResponseEntity<Task> createTask(HttpServletRequest request,@RequestBody CreateTaskDto taskDto){
        Task createNewTask = userService.createTask(request,taskDto);
        return  ResponseEntity.ok().body(createNewTask);
    }
    @PostMapping("/createRepeatedTask")
    public ResponseEntity<String> createRepeatedTask(HttpServletRequest request,@RequestBody CreateTaskDto taskDto){
        String createNewTask = userService.createRepeatingTask(request,taskDto);
        return  ResponseEntity.ok().body(createNewTask);
    }
    @DeleteMapping("/deleteTask/{taskId}")
    public ResponseEntity<Task> deleteTask(HttpServletRequest request,@PathVariable(name = "taskId") int taskId){
        Task deleteTask = userService.deleteTask(request,taskId);
        return ResponseEntity.ok().body(deleteTask);
    }
    @PostMapping("/editTaskDetails")
    public ResponseEntity<Task> editTaskDetails(HttpServletRequest request, @RequestBody EditTaskDetailsDto editTaskDetailsDto){
        Task editDetails = userService.changeTaskDetails(request,editTaskDetailsDto);
        return ResponseEntity.ok().body(editDetails);
    }
}
