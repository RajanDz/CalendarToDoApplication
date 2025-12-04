package com.calendarapp.CalendarTaskAplication.service;

import com.calendarapp.CalendarTaskAplication.model.Role;
import com.calendarapp.CalendarTaskAplication.model.Task;
import com.calendarapp.CalendarTaskAplication.model.User;
import com.calendarapp.CalendarTaskAplication.model.dto.CreateTaskDto;
import com.calendarapp.CalendarTaskAplication.model.dto.EditTaskDetailsDto;
import com.calendarapp.CalendarTaskAplication.repository.RoleRepository;
import com.calendarapp.CalendarTaskAplication.repository.TaskRepository;
import com.calendarapp.CalendarTaskAplication.repository.UserRepository;
import com.calendarapp.CalendarTaskAplication.security.jwt.JwtUtills;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final RoleRepository roleRepository;
    private final JwtUtills jwtUtills;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private String defaultPath = "C:/Users/Rajan44/Desktop/frontend/calendarApp/gallery";
    public UserService(UserRepository userRepository, TaskRepository taskRepository, RoleRepository roleRepository, JwtUtills jwtUtills) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.roleRepository = roleRepository;
        this.jwtUtills = jwtUtills;
    }


    public User registration(String firstname,
                             String lastname,
                             String username,
                             String email,
                             String password,
                             MultipartFile profilePicture) throws IOException {
        Optional<User> findByUsername = userRepository.findByUsername(username);
        Optional<User> findByEmail = userRepository.findByEmail(email);
        if (findByUsername.isPresent()){
            throw new RuntimeException("We already have user with this username. You need to enter unique username.");
        } else if (findByEmail.isPresent()){
            throw new RuntimeException("We already have user with this email. You need to enter unique email.");
        }


        String uniqueFileName = UUID.randomUUID() + "_" + profilePicture.getOriginalFilename();
        Path uploadPath = Paths.get(defaultPath);
        Path imgPath = uploadPath.resolve(uniqueFileName);
        Files.copy(profilePicture.getInputStream(), imgPath, StandardCopyOption.REPLACE_EXISTING);

        Role defaultRole = roleRepository.findById(1).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "We can not find the default role to finish user registration."));
        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);


        User createUser = new User(
                firstname
                ,lastname
                ,username
                ,email
                ,passwordEncoder.encode(password)
                ,"/" + uniqueFileName,
                roles
                );

        return userRepository.save(createUser);
    }
    public String getUserProfileImage(HttpServletRequest request){
        String username = jwtUtills.getUsernameFromJwtToken(request);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"We can not find the user you are looking for by his username.(Return profile img)"));
        return user.getProfilePicture();
    }
    public List<Task> findUsersTask(int userid){
        return userRepository.findByUserId(userid);
    }
    public List<Task> findTasksByDate(HttpServletRequest request,LocalDate date){
        String getUsernameOfUser = jwtUtills.getUsernameFromJwtToken(request);
        User user = userRepository.findByUsername(getUsernameOfUser).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "We can not find user by username: " + getUsernameOfUser));
        List<Task> tasks = taskRepository.findTasksByDate(user.getId(),date);
        if (tasks.isEmpty()){
            throw new RuntimeException("List of tasks for date: " + date + "is empty!");
        }
        tasks.sort((t1,t2) -> t1.getTimeOfStart().compareTo(t2.getTimeOfStart()));
            return  tasks;
    }
    public User findUserByUsername(HttpServletRequest request){
        String getUsernameOfUser = jwtUtills.getUsernameFromJwtToken(request);
        return userRepository.findByUsername(getUsernameOfUser)
                .orElseThrow(() ->
                        new ResponseStatusException
                                (HttpStatus.NOT_FOUND,"We can not find user by his username"));
    }

    public Task findUpcomingTask(HttpServletRequest request){
            String username = jwtUtills.getUsernameFromJwtToken(request);
            User user = userRepository.findByUsername(username).
                    orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "We can not find user with username: " + username));
            List<Task> tasks = taskRepository.findTaskForToday(user.getId(), LocalDate.now(), LocalTime.now());
            if (tasks.isEmpty()){
                return null;
            }
            long closestTask = Long.MAX_VALUE;
            Task upcomingTask = tasks.get(0);
            for (Task task: tasks){
                long minutesToStart = ChronoUnit.MINUTES.between(LocalTime.now(),task.getTimeOfStart());
                if (minutesToStart < closestTask && minutesToStart > 0){
                    upcomingTask = task;
                    closestTask = minutesToStart;
                }
            }
        return upcomingTask;
    }

    public List<Task> getTasksForNext7Days(HttpServletRequest request){
        String username = jwtUtills.getUsernameFromJwtToken(request);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "We can not find the user by his user(next 7 days tasks)"));
        LocalDate tommorow = LocalDate.now().plusDays(1);
        LocalDate sevenDayFromTommorow = tommorow.plusDays(6);
        return taskRepository.findTaskForNext7Days(user.getId(),tommorow,sevenDayFromTommorow);
    }

    public Task createTask(HttpServletRequest request, CreateTaskDto task){
        String username = jwtUtills.getUsernameFromJwtToken(request);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "We can not find the user by his user(create task)"));
        Task newTask = new Task(
                task.getName(),
                task.getDescription()
                ,task.getDateOfStart(),
                task.getTimeOfStart(),
                task.getDateOfEnd()
                ,task.getTimeOfEnd()
                ,task.getStatus()
        );
        taskRepository.save(newTask);
        user.getTasks().add(newTask);
        userRepository.save(user);
        return newTask;
    }

    public String createRepeatingTask(HttpServletRequest request,CreateTaskDto createTaskDto){
        String username = jwtUtills.getUsernameFromJwtToken(request);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "We can not find the user by his user(create task)"));

        int tillYear = createTaskDto.getDateOfStart().getYear();
        LocalDate beginDate = createTaskDto.getDateOfStart();
        LocalDate endTask = LocalDate.of(tillYear,12,31);

        while (!beginDate.isAfter(endTask)){
            Task task = new Task(
                    createTaskDto.getName(),
                    createTaskDto.getDescription(),
                    beginDate,
                    createTaskDto.getTimeOfStart(),
                    beginDate,
                    createTaskDto.getTimeOfEnd(),
                    createTaskDto.getStatus()
            );
            taskRepository.save(task);
            user.getTasks().add(task);
            beginDate = beginDate.plusWeeks(1);
        }
        return "Repeated Task is created!";
    }

    public Task deleteTask(HttpServletRequest request, int taskId){
        String username = jwtUtills.getUsernameFromJwtToken(request);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "We can not find the user by his user(delete task)"));
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "We can not find the task you are looking for"));
        user.getTasks().remove(task);
        userRepository.save(user);
        taskRepository.delete(task);
        return task;
    }
    public Task changeTaskDetails(HttpServletRequest request, EditTaskDetailsDto editDto){
        String username = jwtUtills.getUsernameFromJwtToken(request);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "We can not find the user by his user(edit details task)"));
        Task task = taskRepository.findById(editDto.getTaskId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "We can not find the task you are looking for"));
        boolean ownTask = user.getTasks().stream().anyMatch(u -> u.getId() == task.getId());
        if (!ownTask){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to edit this task");
        }
        if (editDto.getName() != null && !editDto.getName().isEmpty()){
            task.setName(editDto.getName());
        }
        if (editDto.getDescription() != null && !editDto.getDescription().isEmpty()){
            task.setDescription(editDto.getDescription());
        }
        if (editDto.getDateOfStart() != null){
            task.setDateOfStart(editDto.getDateOfStart());
        }
        if (editDto.getTimeOfStart() != null){
            task.setTimeOfStart(editDto.getTimeOfStart());
        }
        if (editDto.getDateOfEnd() != null){
            task.setDateOfEnd(editDto.getDateOfEnd());
        }
        if (editDto.getTimeOfEnd() != null){
            task.setTimeOfEnd(editDto.getTimeOfEnd());
        }
        if (editDto.getStatus() != null && !editDto.getStatus().isEmpty()){
            task.setStatus(editDto.getStatus());
        }
        taskRepository.save(task);
        return task;
    }
}

