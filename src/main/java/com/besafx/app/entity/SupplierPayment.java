package com.besafx.app.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;

@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierPayment implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "supplierPaymentSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SUPPLIER_PAYMENT_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "supplierPaymentSequenceGenerator")
    private Long id;

    private Integer code;

    @ManyToOne
    @JoinColumn(name = "supplier")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "bankTransaction")
    private BankTransaction bankTransaction;

    @ManyToOne
    @JoinColumn(name = "person")
    private Person person;

    @JsonCreator
    public static SupplierPayment Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SupplierPayment supplierPayment = mapper.readValue(jsonString, SupplierPayment.class);
        return supplierPayment;
    }
}
