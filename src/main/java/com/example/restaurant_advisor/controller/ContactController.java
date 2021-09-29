package com.example.restaurant_advisor.controller;

import com.example.restaurant_advisor.model.Contact;
import com.example.restaurant_advisor.repository.ContactRepository;
import com.example.restaurant_advisor.repository.RestaurantRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Contact Controller")
public class ContactController {

    @Autowired
    ContactRepository contactRepository;
    @Autowired
    RestaurantRepository restaurantRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/restaurants/{id}/contact")
    public Contact getContact(@PathVariable int id) {
        log.info("get contact by id {}", id);
        return contactRepository.getExisted(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/restaurants/{id}/contact")
    @CacheEvict(value = "restaurants", allEntries = true)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public String saveOrUpdateContact(@Valid Contact contact, @PathVariable int id) {
        log.info("{} contact {}", contact.isNew() ? "add" : "edit", contact);
        // https://stackoverflow.com/a/11105886
        contact.setRestaurant(restaurantRepository.getExisted(id));
        if(contact.isNew()) {
            contact.setId(id);
        }
        contactRepository.save(contact);

        return "redirect:/restaurants/" + id;
    }
}