package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.entities.Schedule;
import com.udacity.jdnd.course3.critter.repositories.CustomerRepository;
import com.udacity.jdnd.course3.critter.repositories.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repositories.PetRepository;
import com.udacity.jdnd.course3.critter.repositories.ScheduleRepository;
import com.udacity.jdnd.course3.critter.schedule.ScheduleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> findScheduleByPetId(Long id) {
        Pet pet = petRepository.getOne(id);
        return scheduleRepository.findAllByPetsId(pet.getId());
    }

    public List<Schedule> findScheduleByEmployeeId(Long id) {
        Employee employee = employeeRepository.getOne(id);
        return scheduleRepository.findAllByEmployeesId(employee.getId());
    }

    public List<Schedule> findScheduleByCustomerId(Long id) {
        Customer customer = customerRepository.getOne(id);
        return scheduleRepository.findAllByPetsIn(customer.getPets());
    }

    public Schedule fromRequestSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        List<Long> employeeIds = scheduleDTO.getEmployeeIds();
        List<Employee> employees = employeeIds
            .stream()
            .map(id -> employeeRepository.getOne(id))
            .collect(Collectors.toList());
        schedule.setEmployees(employees);
        List<Long> petIds = scheduleDTO.getPetIds();
        List<Pet> pets = petIds
            .stream()
            .map(id -> petRepository.getOne(id))
            .collect(Collectors.toList());
        schedule.setPets(pets);
        schedule.setDate(scheduleDTO.getDate());
        schedule.setActivities(scheduleDTO.getActivities());
        return schedule;
    }

    public ScheduleDTO forResponseSchedule(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setEmployeeIds(
            schedule.getEmployees()
                .stream()
                .map(Employee::getId)
                .collect(Collectors.toList())
        );
        scheduleDTO.setPetIds(
                schedule.getPets()
                    .stream()
                    .map(Pet::getId)
                    .collect(Collectors.toList())
        );
        scheduleDTO.setDate(schedule.getDate());
        scheduleDTO.setActivities(schedule.getActivities());
        return scheduleDTO;
    }
}
