package com.example.restaurant_advisor.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email", name = "users_unique_email_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"password", "reviews"})
public class User extends BaseEntity {

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Email is not correct")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @Column(name = "firstName")
    @NotBlank(message = "First name cannot be empty")
    private String firstName;

    @Column(name = "lastName")
    @NotBlank(message = "Last name cannot be empty")
    private String lastName;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password cannot be empty")
    private String password;

    @Transient
    @NotBlank(message = "Password2 cannot be empty")
    private String password2;

    @Column(name = "active", nullable = false, columnDefinition = "bool default false")
    private boolean active;

    private String activationCode;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role"}, name = "user_roles_unique")})
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @OrderBy("date DESC")
    //https://stackoverflow.com/a/44988100/548473
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Review> reviews;

    public User(Integer id, String email, String firstName, String lastName, String password, boolean active, Set<Role> roles, List<Review> reviews) {
        super(id);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.active = active;
        this.reviews = reviews;
        setRoles(roles);
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = CollectionUtils.isEmpty(roles) ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(roles);
    }

    // https://github.com/spring-projects/spring-data-rest/issues/1368
    @PostLoad
    private void postLoadPassword2(){
        this.password2 = this.password;
    }

}