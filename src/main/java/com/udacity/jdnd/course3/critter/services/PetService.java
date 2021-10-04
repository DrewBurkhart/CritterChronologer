package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.repositories.CustomerRepository;
import com.udacity.jdnd.course3.critter.repositories.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PetService {

    @Autowired
    private PetRepository petRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public Pet save(Pet pet) {
        Pet savedPet = petRepository.save(pet);
        Customer customer = customerRepository.getOne(pet.getCustomer().getId());
        List<Pet> pets = customer.getPets() != null ? customer.getPets() : new ArrayList<>();
        pets.add(pet);
        customer.setPets(pets);
        customerRepository.save(customer);
        return savedPet;
    }

    public Pet findById(Long id) {
        return petRepository.getOne(id);
    }

    public List<Pet> findAll() {
        return petRepository.findAll();
    }

    public List<Pet> findAllByCustomerId(Long id) {
        return customerRepository.getOne(id).getPets();
    }
}
