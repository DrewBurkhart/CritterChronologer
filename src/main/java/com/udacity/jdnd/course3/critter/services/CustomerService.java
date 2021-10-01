package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.repositories.CustomerRepository;
import com.udacity.jdnd.course3.critter.repositories.PetRepository;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PetRepository petRepository;

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer findByPetId(Long petId) {
        return petRepository.getOne(petId).getCustomer();
    }

    public Customer fromRequestCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setName((customerDTO.getName()));
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setNotes(customerDTO.getNotes());
        List<Long> petIds = customerDTO.getPetIds();
        return customer;
    }

    public CustomerDTO forResponseCustomer(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setNotes(customer.getNotes());
        if (customer.getPets() != null) {
            // https://www.baeldung.com/java-8-collectors#1-collectorstolist
            // https://www.geeksforgeeks.org/stream-map-java-examples/
            List<Long> petIds = customer.getPets().stream().map(Pet::getId).collect(Collectors.toList());
            customerDTO.setPetIds(petIds);
        }
        return customerDTO;
    }
}
