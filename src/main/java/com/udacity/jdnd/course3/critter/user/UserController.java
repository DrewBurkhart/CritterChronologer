package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Employee;
import com.udacity.jdnd.course3.critter.services.CustomerService;
import com.udacity.jdnd.course3.critter.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            Customer customer = CustomerDTO.fromRequestCustomer(customerDTO);
            Customer savedCustomer = customerService.save(customer);
            return CustomerDTO.forResponseCustomer(savedCustomer);
        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Unable to save Customer. Please check the data and try again."
            );
        }
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        try {
            List<Customer> customers = customerService.findAll();
            return customers
                .stream()
                .map(CustomerDTO::forResponseCustomer)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find any Customers");
        }
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId) {
        try {
            return CustomerDTO.forResponseCustomer(customerService.findByPetId(petId));
        } catch (Exception e) {
            // https://www.baeldung.com/spring-response-status-exception#2-different-status-code---same-exception-type
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Unable to find Customer with a Pet matching that ID"
            );
        }
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        try {
            Employee employee = EmployeeDTO.fromRequestEmployee(employeeDTO);
            Employee savedEmployee = employeeService.save(employee);
            return EmployeeDTO.forResponseEmployee(savedEmployee);
        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Unable to save Employee. Please check the data and try again."
            );
        }
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        try {
            return EmployeeDTO.forResponseEmployee(employeeService.findById(employeeId));
        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Unable to find Employee matching that ID."
            );
        }
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        try {
            employeeService.setAvailability(daysAvailable, employeeId);
        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Unable to set availability. Please check the data and try again."
            );
        }
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        try {
            return employeeService.findBySkill(employeeDTO.getSkills(), employeeDTO.getDate())
                .stream()
                .map(EmployeeDTO::forResponseEmployee)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Unable to find Employee matching those parameters."
            );
        }
    }

}
