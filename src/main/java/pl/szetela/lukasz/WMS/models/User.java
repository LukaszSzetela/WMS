package pl.szetela.lukasz.WMS.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Size(min = 1, max = 15)
    @NotNull
    private String firstName;

    @Size(min = 1, max = 15)
    @NotNull
    private String lastName;

    @Email
    @Column(unique = true)
    @NotNull
    @Size(min = 1, max = 20)
    private String email;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(unique = true)
    private String username;

    @Size(min = 1, max = 60)
    @NotNull
    private String password;

    @NotNull
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "executor", fetch = FetchType.EAGER)
    private List<Order> executorOrders = new ArrayList<>();

    @OneToMany(mappedBy = "orderer", fetch = FetchType.EAGER)
    private List<Order> customerOrders = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

}