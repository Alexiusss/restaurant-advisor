package com.example.restaurant_advisor.model;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"password"})
public class User  extends BaseEntity{

    public User(Integer id, String email, String firstName, String lastName, String password, boolean active, Collection<Role> roles) {
        this(email, firstName, lastName, password, active, EnumSet.copyOf(roles));
        this.id = id;
    }

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "password")
    private String password;

    @Column(name = "active", nullable = false, columnDefinition = "bool default true")
    private boolean active;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role"}, name = "user_roles_unique")})
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;
}