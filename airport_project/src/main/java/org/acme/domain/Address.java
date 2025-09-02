package org.acme.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "address")
public class Address {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(name = "address_name", nullable = false)
    private String addressName;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "country", nullable = false)
    private String country;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User user;

    public Address() {
    }

    public Address(String addressName, String city, String country, User user) {
        this.addressName = addressName;
        this.city = city;
        this.country = country;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public String getAddressName() {
        return addressName;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public User getUser() {
        return user;
    }
}
