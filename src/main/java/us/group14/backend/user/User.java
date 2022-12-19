package us.group14.backend.user;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import us.group14.backend.account.Account;
import us.group14.backend.transaction.Transaction;
import us.group14.backend.views.AllUserInfoView;
import us.group14.backend.views.ContactsView;
import us.group14.backend.views.TransactionAndAccountView;
import us.group14.backend.views.UserAndAccountView;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    @JsonView({UserAndAccountView.class, TransactionAndAccountView.class, ContactsView.class, AllUserInfoView.class})
    private Long id;

    @Column(nullable = false, unique = true)
    @JsonView({UserAndAccountView.class, TransactionAndAccountView.class, ContactsView.class, AllUserInfoView.class})
    private String username;

    @Column(nullable = false)
    @JsonView({UserAndAccountView.class, TransactionAndAccountView.class, ContactsView.class, AllUserInfoView.class})
    private String email;

    @Enumerated(EnumType.STRING)
    @JsonView({AllUserInfoView.class})
    private UserRole userRole;

    @Column(nullable = false)
    private String password;

    private Boolean locked = false;
    private Boolean enabled = false;

//    @OneToMany(fetch = FetchType.EAGER)
//    @JsonView({AllUserInfoView.class})
//    private Set<Contact> contacts;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    @JsonView({UserAndAccountView.class, AllUserInfoView.class})
    private Account account;

    public User(String username, String email, String password, UserRole userRole) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userRole.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

//    public List<User> getContactUsers() {
//        return contacts.stream().map(contact -> contact.getContact()).toList();
//    }
//
//    public Optional<Contact> findContact(User contact) {
//        return contacts.stream().filter(c -> c.getContact().equals(contact)).findFirst();
//    }
//
//    public void addContact(Contact contact) {
//        this.contacts.add(contact);
//    }
//
//    public void deleteContact(Contact contact) {
//        this.contacts.remove(contact);
//    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", userRole=" + userRole +
                ", password='" + password + '\'' +
                ", locked=" + locked +
                ", enabled=" + enabled +
//                ", contacts=" + contacts.stream().map(Contact::getId).toList() +
                ", account=" + account.getId() +
                '}';
    }
}
