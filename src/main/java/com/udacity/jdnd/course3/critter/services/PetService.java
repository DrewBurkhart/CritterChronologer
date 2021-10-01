package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.repositories.CustomerRepository;
import com.udacity.jdnd.course3.critter.repositories.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PetService {

    @Autowired
    private PetRepository petRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public Pet save(Pet pet) {
        return petRepository.save(pet);
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
