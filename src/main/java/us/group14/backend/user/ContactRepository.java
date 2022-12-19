package us.group14.backend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query("SELECT c FROM Contact c WHERE c.user.id = ?1 AND c.contact.id = ?2")
    Optional<Contact> getContactFromUser(Long userId, Long contactUserId);

    @Query("SELECT c FROM Contact c WHERE c.user.id = ?1")
    Set<Contact> getContactsForUser(Long userId);

}
