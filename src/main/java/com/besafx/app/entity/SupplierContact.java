package com.besafx.app.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class SupplierContact implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "supplierContactSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SUPPLIER_CONTACT_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "supplierContactSequenceGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contact")
    private Contact contact;

    @ManyToOne
    @JoinColumn(name = "supplier")
    private Supplier supplier;

    @JsonCreator
    public static SupplierContact Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SupplierContact supplierContact = mapper.readValue(jsonString, SupplierContact.class);
        return supplierContact;
    }
}
