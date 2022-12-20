package us.group14.backend.user;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ContactRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;

    @AfterEach
    void tearDown() {
        contactRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getContactFromUser() {
        User user1 = userRepository.save(new User("bob", "bob@email.com", "password", UserRole.USER));
        User user2 = userRepository.save(new User("john", "john@email.com", "password", UserRole.USER));

        Contact contact = contactRepository.save(new Contact(user1, user2));

        Optional<Contact> foundContact = contactRepository.getContactFromUser(user1.getId(), user2.getId());
        assertThat(contact.equals(foundContact.get())).isTrue();
    }

    @Test
    void getContactsForUser() {
        User user1 = userRepository.save(new User("bob", "bob@email.com", "password", UserRole.USER));
        User user2 = userRepository.save(new User("john", "john@email.com", "password", UserRole.USER));

        Contact contact = contactRepository.save(new Contact(user1, user2));

        Set<Contact> contacts = contactRepository.getContactsForUser(user1.getId());

        assertThat(contact).isNotNull();
        assertThat(contacts.stream().toList()).asList().contains(contact);
    }
}