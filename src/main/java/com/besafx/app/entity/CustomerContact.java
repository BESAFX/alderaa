package com.besafx.app.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;

@Data
@Entity
public class CustomerContact implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "customerContactSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "CUSTOMER_CONTACT_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "customerContactSequenceGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contact")
    private Contact contact;

    @ManyToOne
    @JoinColumn(name = "customer")
    private Customer customer;

    @JsonCreator
    public static CustomerContact Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CustomerContact customerContact = mapper.readValue(jsonString, CustomerContact.class);
        return customerContact;
    }
}
