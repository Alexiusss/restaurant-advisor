package com.example.restaurant_advisor.util;

import com.example.restaurant_advisor.controller.MatcherFactory;
import com.example.restaurant_advisor.model.Contact;
import com.example.restaurant_advisor.model.User;
import lombok.experimental.UtilityClass;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static com.example.restaurant_advisor.util.RestaurantTestUtil.RESTAURANT1;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@UtilityClass
public class ContactTestUtil {
    public static final MatcherFactory.Matcher<Contact> CONTACT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Contact.class, "restaurant");

    public static final Contact CONTACT1 = new Contact(RESTAURANT1.getId(), "restaurant1 address", "www.restaurant1.com", "restaurant1@email", "11111111", null);

    public static MockHttpServletRequestBuilder saveContact(String url, Contact contact) {
        return post(url, contact.getId())
                .param("address", contact.getAddress())
                .param("email", contact.getEmail())
                .param("phone_number", contact.getPhone_number())
                .param("website", contact.getWebsite())
                .with(csrf());
    }
}