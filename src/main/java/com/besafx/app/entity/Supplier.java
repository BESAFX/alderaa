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
public class Supplier implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "supplierSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SUPPLIER_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "supplierSequenceGenerator")
    private Long id;

    private Integer code;

    private String taxCode;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registerDate;

    private String email;

    @Transient
    private Double openCash;

    @Column(columnDefinition = "boolean default true")
    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name = "contact")
    private Contact contact;

    @OneToMany(mappedBy = "supplier")
    private List<BillPurchase> billPurchases = new ArrayList<>();

    @OneToMany(mappedBy = "supplier")
    private List<SupplierPayment> supplierPayments = new ArrayList<>();

    @OneToMany(mappedBy = "supplier")
    private List<SupplierContact> supplierContacts = new ArrayList<>();

    @JsonCreator
    public static Supplier Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Supplier supplier = mapper.readValue(jsonString, Supplier.class);
        return supplier;
    }

    public Double getTotalPrice() {
        try {
            return this.billPurchases.stream().mapToDouble(BillPurchase::getTotalPriceAfterDiscountAndVat).sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getPaid() {
        try {
            return this.supplierPayments.stream().mapToDouble(supplierPayment -> supplierPayment.getBankTransaction().getAmount()).sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getRemain() {
        try {
            return this.getTotalPrice() - this.getPaid();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Date getLastPaymentDate() {
        try {
            return this.supplierPayments.stream().map(supplierPayment -> supplierPayment.getBankTransaction().getDate()).max(Date::compareTo).get();
        } catch (Exception ex) {
            return null;
        }
    }

    public Date getLastBillDate() {
        try {
            return this.billPurchases.stream().map(BillPurchase::getWrittenDate).max(Date::compareTo).get();
        } catch (Exception ex) {
            return null;
        }
    }
}
