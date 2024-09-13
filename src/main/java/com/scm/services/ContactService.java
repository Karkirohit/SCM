package com.scm.services;

import java.util.List;

import org.springframework.data.domain.Page;
import com.scm.entities.Contact;
import com.scm.entities.User;

public interface ContactService {

    // save
    Contact save(Contact contact);

    // update
    Contact update(Contact contact);

    // get all data
    List<Contact> getAll();

    // get by id
    Contact getById(String id);

    // delete
    void delete(String id);

    // searh user contact
    Page<Contact> searchByName(String name, int size, int page, String sortBy, String order, User user);

    Page<Contact> searchByEnail(String email, int size, int page, String sortBy, String order, User user);

    Page<Contact> searchByPhoneNumber(String phoneNumber, int size, int page, String sortBy, String order, User user);

    // get all the contact of the user
    List<Contact> getByUserId(String usetId);

    Page<Contact> getByUser(User user, int page, int size, String sortField, String sortDirection);
}
