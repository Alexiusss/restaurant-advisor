package com.example.restaurant_advisor.controller;

import com.example.restaurant_advisor.model.Contact;
import com.example.restaurant_advisor.model.Restaurant;
import com.example.restaurant_advisor.repository.ContactRepository;
import com.example.restaurant_advisor.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;

import static com.example.restaurant_advisor.util.ControllerUtils.getErrors;

@Controller
@Slf4j
public class ContactController {

    @Autowired
    ContactRepository contactRepository;
    @Autowired
    RestaurantRepository restaurantRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/main/{id}/contact")
    @CacheEvict(value = "restaurants", allEntries = true)
    public String addContact(@Valid Contact contact, BindingResult bindingResult,
                             Model model, @PathVariable int id) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("contact", contact);
            Restaurant restaurant = restaurantRepository.getWithReviewsAndContact(id).orElseThrow();
            model.addAttribute("restaurant", restaurant);
            return "restaurant";
        } else {
            log.info("{} contact {}", contact.isNew()? "add":"edit", contact);
            // https://stackoverflow.com/a/11105886
            contact.setRestaurant(restaurantRepository.findById(id).orElseThrow());

            contactRepository.save(contact);

            model.addAttribute("contact", null);
        }
        return "redirect:/main/" + id;
    }
}