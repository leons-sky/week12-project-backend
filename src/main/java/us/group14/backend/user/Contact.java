package us.group14.backend.user;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import us.group14.backend.views.AllUserInfoView;
import us.group14.backend.views.ContactsView;
import us.group14.backend.views.UserAndAccountView;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "contacts")
public class Contact {

    @Id
    @SequenceGenerator(
            name = "contact_sequence",
            sequenceName = "contact_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "contact_sequence"
    )
    @JsonView({ContactsView.class, UserAndAccountView.class, AllUserInfoView.class})
    private Long id;

    // Person adding a contact
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    // Person being added as a contact
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contact_id")
    @JsonView(ContactsView.class)
    private User contact;

    public Contact(User user, User contact) {
        this.user = user;
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", user=" + user.getId() +
                ", contact=" + contact.getId() +
                '}';
    }
}
