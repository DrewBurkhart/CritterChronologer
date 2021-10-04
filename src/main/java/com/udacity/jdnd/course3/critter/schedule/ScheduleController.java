package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.entities.Schedule;
import com.udacity.jdnd.course3.critter.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        try {
            Schedule schedule = ScheduleDTO.fromRequestSchedule(scheduleDTO);
            List<Long> employeeIds = scheduleDTO.getEmployeeIds();
            List<Long> petIds = scheduleDTO.getPetIds();
            Schedule savedSchedule = scheduleService.save(schedule, employeeIds, petIds);
            return ScheduleDTO.forResponseSchedule(savedSchedule);
        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Unable to save Schedule. Please check the data and try again."
            );
        }
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        try {
            List<Schedule> schedules = scheduleService.findAll();
            return schedules
                .stream()
                .map(ScheduleDTO::forResponseSchedule)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not find any Schedules");
        }
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        try {
            List<Schedule> schedules = scheduleService.findScheduleByPetId(petId);
            return schedules
                .stream()
                .map(ScheduleDTO::forResponseSchedule)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Could not find any Schedules with a Pet matching that ID"
            );
        }
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        try {
            List<Schedule> schedules = scheduleService.findScheduleByEmployeeId(employeeId);
            return schedules
                .stream()
                .map(ScheduleDTO::forResponseSchedule)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Could not find any Schedules with an Employee matching that ID"
            );
        }
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        try {
            List<Schedule> schedules = scheduleService.findScheduleByCustomerId(customerId);
            return schedules
                    .stream()
                    .map(ScheduleDTO::forResponseSchedule)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Could not find any Schedules with a Customer matching that ID"
            );
        }
    }
}
