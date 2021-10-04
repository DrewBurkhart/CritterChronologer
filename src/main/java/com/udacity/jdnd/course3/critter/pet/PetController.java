package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.entities.Customer;
import com.udacity.jdnd.course3.critter.entities.Pet;
import com.udacity.jdnd.course3.critter.services.CustomerService;
import com.udacity.jdnd.course3.critter.services.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetService petService;
    @Autowired
    private CustomerService customerService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        try {
            Customer customer = customerService.findById(petDTO.getOwnerId());
            Pet pet = PetDTO.fromRequestPet(petDTO, customer);
            Pet savedPet = petService.save(pet);
            return PetDTO.forResponsePet(savedPet);
        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Unable to save Pet. Please check the data and try again."
            );
        }
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        try {
            return PetDTO.forResponsePet(petService.findById(petId));
        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Unable to find Pet matching that ID."
            );
        }
    }

    @GetMapping
    public List<PetDTO> getPets(){
        try {
            List<Pet> pets = petService.findAll();
            return pets
                .stream()
                .map(PetDTO::forResponsePet)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find any Pets");
        }
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        try {
            List<Pet> pets = petService.findAllByCustomerId(ownerId);
            return pets
                .stream()
                .map(PetDTO::forResponsePet)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Could not find any Pets with an Owner matching that ID"
            );
        }
    }
}
