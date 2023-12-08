package ru.practicum.user;

import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 254)
    private String email;
    @Column(nullable = false, length = 250)
    private String name;
    @Formula(value = "(SELECT CASE WHEN AVG(m.mark) IS NULL THEN 0.0 ELSE AVG(m.mark) END " +
            "FROM marks AS m JOIN events AS e ON m.event_id = e.id " +
            "WHERE e.initiator_id = id)")
    private double rating;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}