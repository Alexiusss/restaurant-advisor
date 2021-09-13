package com.example.restaurant_advisor.repository;

import com.example.restaurant_advisor.model.Contact;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface ContactRepository extends BaseRepository<Contact> {
}