package com.example.fluxkart.repository;

import com.example.fluxkart.entity.Contact;
import com.example.fluxkart.entity.LinkPrecedence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Long> {
    //Fetch all rows that share a given e-mail
    List<Contact> findByEmail(String email);

    //Fetch all rows that share a given phone number
    List<Contact> findByPhoneNumber(String phoneNumber);

    //Fetch rows that match on either column
    List<Contact> findByEmailOrPhoneNumber(String email, String phoneNumber);


}