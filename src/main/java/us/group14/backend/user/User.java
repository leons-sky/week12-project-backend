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

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
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
    @JsonView(UserView.class)
    private Long id;

    @Column(nullable = false, unique = true)
    @JsonView(UserView.class)
    private String username;

    @Column(nullable = false)
    @JsonView(UserView.class)
    private String email;

    @Enumerated(EnumType.STRING)
    @JsonView(UserView.class)
    private UserRole userRole;

    @Column(nullable = false)
    private String password;

    private Boolean locked = false;
    private Boolean enabled = false;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name="contacts",
//            joinColumns=@JoinColumn(name="userId"),
//            inverseJoinColumns=@JoinColumn(name="contactId")
//    )
//    private Set<User> contacts;
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name="contacts",
//            joinColumns=@JoinColumn(name="contactId"),
//            inverseJoinColumns=@JoinColumn(name="userId")
//    )
//    private Set<User> contactsOf;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
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


//    public void addContact(User contact) {
//        this.contacts.add(contact);
//
//    }
//
//    public void deleteContact(User contact) {
//        this.contacts.remove(contact);
//    }
}
