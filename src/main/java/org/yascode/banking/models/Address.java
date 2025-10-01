package org.yascode.banking.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "addresses")
public class Address extends AbstractEntity {

    private String street;

    @Column(name = "house_number")
    private Integer houseNumber;

    @Column(name = "zip_code")
    private Integer zipCode;

    private String city;

    private String county;

    @OneToOne
    @JoinColumn(name = "id_user")
    private User user;
}
