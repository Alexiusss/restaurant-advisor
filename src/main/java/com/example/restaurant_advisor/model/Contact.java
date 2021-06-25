package com.example.restaurant_advisor.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "contacts", uniqueConstraints = {@UniqueConstraint(columnNames = {"adress", "phone_number"}, name = "contacts_unique_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"restaurant"})
public class Contact {

    // https://www.baeldung.com/jpa-one-to-one
    @Id
    @Column(name = "restaurant_id")
    private Integer id;

    @Column(name = "adress", nullable = false)
    String adress;

    @Column(name = "website")
    String website;

    @Column(name = "email")
    String email;

    @Column(name = "phone_number", nullable = false)
    String phone_number;

    @OneToOne(fetch = FetchType.LAZY)
    // https://vladmihalcea.com/the-best-way-to-map-a-onetoone-relationship-with-jpa-and-hibernate/
    @MapsId
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
}