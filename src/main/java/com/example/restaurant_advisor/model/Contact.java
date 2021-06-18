package com.example.restaurant_advisor.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "contacts", uniqueConstraints = {@UniqueConstraint(columnNames = {"adress", "phone_number"}, name = "contacts_unique_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(callSuper = true)
public class Contact extends BaseEntity {

    @Column(name = "adress", nullable = false)
    String adress;

    @Column(name = "website")
    String website;

    @Column(name = "email")
    String email;

    @Column(name = "phone_number", nullable = false)
    String phone_number;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
}