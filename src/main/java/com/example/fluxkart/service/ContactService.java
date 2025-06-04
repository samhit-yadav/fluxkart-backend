package com.example.fluxkart.service;

import com.example.fluxkart.dto.ContactResponseDTO;
import com.example.fluxkart.entity.Contact;
import com.example.fluxkart.entity.LinkPrecedence;
import com.example.fluxkart.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ContactService {

    private ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public ContactResponseDTO identifyContact(String email,String phoneNumber){
        Set<Contact> allContacts = new HashSet<>();
        List<Contact>initialMatches = contactRepository.findByEmailOrPhoneNumber(email, phoneNumber);

        allContacts.addAll(initialMatches);
        Queue<Contact>queue = new LinkedList<>(initialMatches);
        Set<String>visitedEmails = new HashSet<>();
        Set<String>visitedPhoneNumbers = new HashSet<>();

        while(!queue.isEmpty()){

            Contact current = queue.poll();

            boolean emailVisited = current.getEmail()!=null && visitedEmails.contains(current.getEmail());
            boolean phoneVisited = current.getPhoneNumber()!=null && visitedPhoneNumbers.contains(current.getPhoneNumber());

            if(emailVisited && phoneVisited){
                continue;
            }
            if(current.getEmail()!=null) {
                visitedEmails.add(current.getEmail());
            }
            if(current.getPhoneNumber()!=null){
                visitedPhoneNumbers.add(current.getPhoneNumber());
            }

            List<Contact>related = contactRepository.findByEmailOrPhoneNumber(current.getEmail(),current.getPhoneNumber());
            for(Contact c : related){
                if(!allContacts.contains(c)){
                    allContacts.add(c);
                    queue.offer(c);
                }
            }
        }
        Contact primary = allContacts.stream()
                .filter(c -> c.getLinkPrecedence() == LinkPrecedence.PRIMARY)
                .min(Comparator.comparing(Contact::getCreatedAt))
                .orElseGet(() ->
                        allContacts.stream()
                                .min(Comparator.comparing(Contact::getCreatedAt))
                                .orElse(null));
//            Among allContacts, choose the earliest created PRIMARY.
//            If none are PRIMARY, just pick the oldest contact.


//        If no contact exists at all, create a brand new PRIMARY contact.
        if(primary == null) {
            Contact newPrimary = Contact.builder()
                    .email(email)
                    .phoneNumber(phoneNumber)
                    .linkPrecedence(LinkPrecedence.PRIMARY)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            primary = contactRepository.save(newPrimary);
            allContacts.add(primary);
        }
//            All contacts except primary should:
//            Be marked as SECONDARY
//            Have linkedId = primary.id
            for (Contact c : allContacts) {
                if (!Objects.equals(c.getId(), primary.getId()) &&
                        (c.getLinkPrecedence() != LinkPrecedence.SECONDARY || !Objects.equals(primary.getId(), c.getLinkedId()))) {
                    c.setLinkPrecedence(LinkPrecedence.SECONDARY);
                    c.setLinkedId(primary.getId());
                    c.setUpdatedAt(LocalDateTime.now());
                    contactRepository.save(c);
                }
            }

            boolean isNewEmail = email != null && allContacts.stream().noneMatch(c -> email.equals(c.getEmail()));
            boolean isNewPhoneNumber = phoneNumber != null && allContacts.stream().noneMatch(c -> phoneNumber.equals(c.getPhoneNumber()));

            if (isNewEmail || isNewPhoneNumber) {
                Contact newSecondary = Contact.builder()
                        .email(isNewEmail ? email : null)
                        .phoneNumber(isNewPhoneNumber ? phoneNumber : null)
                        .linkPrecedence(LinkPrecedence.SECONDARY)
                        .linkedId(primary.getId())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
                contactRepository.save(newSecondary);
                allContacts.add(newSecondary);
            }

            Set<String> emails = new HashSet<>();
            Set<String> phoneNumbers = new HashSet<>();
            List<Long> secondaryIds = new ArrayList<>();

            if (primary.getEmail() != null) emails.add(primary.getEmail());
            if (primary.getPhoneNumber() != null) phoneNumbers.add(primary.getPhoneNumber());

            for (Contact c : allContacts) {
                if (!Objects.equals(c.getId(), primary.getId())) {
                    if (c.getEmail() != null) emails.add(c.getEmail());
                    if (c.getPhoneNumber() != null) phoneNumbers.add(c.getPhoneNumber());
                    secondaryIds.add(c.getId());
                }
            }

        return new ContactResponseDTO(
                primary.getId(),
                new ArrayList<>(emails),
                new ArrayList<>(phoneNumbers),
                secondaryIds
        );

    }

}