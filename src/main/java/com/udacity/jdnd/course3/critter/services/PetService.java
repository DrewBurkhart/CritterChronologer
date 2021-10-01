package com.udacity.jdnd.course3.critter.services;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.repositories.CustomerRepository;
import com.udacity.jdnd.course3.critter.repositories.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public Pet fromRequestPet(PetDTO petDTO) {
        Pet pet = new Pet();
        pet.setName(petDTO.getName());
        pet.setBirthDate(petDTO.getBirthDate());
        pet.setType(petDTO.getType());
        pet.setNotes(petDTO.getNotes());
        try {
            Customer customer = customerRepository.getOne(petDTO.getOwnerId());
            pet.setCustomer(customer);
            return pet;
        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Unable to save Pet. Please check your customer information and try again."
            );
        }
    }

    public PetDTO forResponsePet(Pet pet) {
        PetDTO petDTO = new PetDTO();
        petDTO.setId(pet.getId());
        petDTO.setName(pet.getName());
        petDTO.setBirthDate(pet.getBirthDate());
        petDTO.setType(pet.getType());
        petDTO.setNotes(pet.getNotes());
        petDTO.setOwnerId(pet.getCustomer().getId());
        return petDTO;
    }
}
