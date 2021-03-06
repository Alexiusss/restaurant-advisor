package com.example.restaurant_advisor.model;

import com.example.restaurant_advisor.HasId;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "contacts", uniqueConstraints = {@UniqueConstraint(columnNames = {"address", "phone_number"}, name = "contacts_unique_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"restaurant"})
public class Contact implements HasId {

    // https://www.baeldung.com/jpa-one-to-one
    @Id
    @Column(name = "restaurant_id")
    private Integer id;

    @Column(name = "address", nullable = false)
    @NotBlank(message = "Please fill the address")
    @Size(max = 128)
    String address;

    @Column(name = "website")
    @Size(max = 128)
    String website;

    @Column(name = "email")
    @Email(message = "Email is not correct")
    String email;

    @Column(name = "phone_number", nullable = false)
    @NotBlank(message = "Please fill the phone number")
    @Pattern(regexp = "([\\+]*[0-9]{1,4}\\s?[\\(]*\\d[0-9]{2,4}[\\)]*\\s?\\d{3}[-]*\\d{2}[-]*\\d{2})"
            , message = "Please fill the phone number in format +1 (234) 567-89-10")
    String phone_number;

    // https://stackoverflow.com/a/54405532
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    // https://vladmihalcea.com/the-best-way-to-map-a-onetoone-relationship-with-jpa-and-hibernate/
    @MapsId
    @JoinColumn(name = "restaurant_id", nullable = false, updatable = false)
    private Restaurant restaurant;
}