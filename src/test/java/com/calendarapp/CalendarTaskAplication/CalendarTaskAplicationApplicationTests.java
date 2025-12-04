package com.calendarapp.CalendarTaskAplication;

import com.calendarapp.CalendarTaskAplication.model.Role;
import com.calendarapp.CalendarTaskAplication.model.Task;
import com.calendarapp.CalendarTaskAplication.model.Team;
import com.calendarapp.CalendarTaskAplication.model.User;
import com.calendarapp.CalendarTaskAplication.model.dto.CreateTaskDto;
import com.calendarapp.CalendarTaskAplication.repository.RoleRepository;
import com.calendarapp.CalendarTaskAplication.repository.TaskRepository;
import com.calendarapp.CalendarTaskAplication.repository.TeamRepository;
import com.calendarapp.CalendarTaskAplication.repository.UserRepository;
import com.calendarapp.CalendarTaskAplication.service.UserService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@SpringBootTest
class CalendarTaskAplicationApplicationTests {
    private static Logger logger = LoggerFactory.getLogger(CalendarTaskAplicationApplicationTests.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private TeamRepository teamRepository;
	@Test
	void contextLoads() {
	}


    @Test
    void createUser(){
        Role role = new Role("TestRole", "Testiram rolu");
        Set<Role> roles = Set.of(role);
        roleRepository.save(role);
        User user = new User("Marko", "Marković", "rajan44", "marko@mail.com", passwordEncoder.encode("Rajan123"), "slika.jpg", roles);
        userRepository.save(user);
    }

    @Test
    void getTasksByDate(){
        List<Task> getTasksByDate = taskRepository.findTaskForToday(1, LocalDate.now(), LocalTime.now());
        logger.info("tasks: {}", getTasksByDate);
    }

//    @Test
//    void createTask(){
//        CreateTaskDto taskDto = new CreateTaskDto("Test task iz test klase", "ovo je task koji testiram da vidim da li ce se sacuvati ispravno", LocalDate.now(),LocalTime.now(),LocalDate.now().plusDays(1),LocalTime.now(),"Important");
//        Task task = userService.createTask(taskDto);
//        logger.info("Task: {}", task);
//    }

    @Test
    void createTeam() {
        User user = userRepository.findById(1).orElseThrow();
        Team newTeam = new Team("AKMF Bastion", "Ovo je amaterski tim malog fudbala", user);
        teamRepository.save(newTeam);
    }
    @Test
    void addMemberToTeam(){
        User user = userRepository.findById(2).orElseThrow();
        Team team = teamRepository.findById(1).orElseThrow();
        boolean isUserInTeam = team.getMembers().stream().anyMatch(member -> Objects.equals(member.getId(), user.getId()));
        if (isUserInTeam){
            logger.info("User is already in this team");
            return;
        }
        logger.info("User is added to team");
        team.getMembers().add(user);
        teamRepository.save(team);
    }

    @Test
    void createTaskForTeam(){
        Team team = teamRepository.findById(1).orElseThrow();
        Task task = new Task("Prvi team task","Ovo je prvi team task",LocalDate.now(),LocalTime.now(),LocalDate.now().plusDays(2),LocalTime.now(),"Important");
        taskRepository.save(task);
    }
    @Test
    void getTeamTasks(){
        Team team = teamRepository.findById(1).orElseThrow();
        List<Task> tasks = taskRepository.findTasksByTeamId(team.getId());
        logger.info("Tasks: {}", tasks);
    }
    @Test
    void getMembersOfTeam(){
        Team team = teamRepository.findById(1).orElseThrow();
        logger.info("Members of team: {}", team.getMembers());
    }
}

