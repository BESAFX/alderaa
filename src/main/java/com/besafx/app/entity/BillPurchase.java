package com.besafx.app.entity;

import com.besafx.app.entity.enums.BillPurchaseCondition;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillPurchase implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "billPurchaseSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "BILL_PURCHASE_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "billPurchaseSequenceGenerator")
    private Long id;

    private Long code;

    private Double discount;

    private Double transferFees;

    @ManyToOne
    @JoinColumn(name = "supplier")
    private Supplier supplier;

    @Temporal(TemporalType.TIMESTAMP)
    private Date writtenDate;

    @Enumerated(EnumType.STRING)
    private BillPurchaseCondition condition;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String note;

    @ManyToOne
    @JoinColumn(name = "person")
    private Person person;

    @OneToMany(mappedBy = "billPurchase")
    private List<BillPurchaseAttach> billPurchaseAttaches = new ArrayList<>();

    @OneToMany(mappedBy = "billPurchase")
    private List<BillPurchaseProduct> billPurchaseProducts = new ArrayList<>();

    @JsonCreator
    public static BillPurchase Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BillPurchase billPurchase = mapper.readValue(jsonString, BillPurchase.class);
        return billPurchase;
    }

    public Double getTotalPrice() {
        try {
            return this.billPurchaseProducts
                    .stream()
                    .mapToDouble(billPurchaseProduct -> billPurchaseProduct.getQuantity() * billPurchaseProduct.getUnitPurchasePrice())
                    .sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getTotalVat() {
        try {
            return this.billPurchaseProducts
                    .stream()
                    .mapToDouble(billPurchaseProduct -> billPurchaseProduct.getQuantity() * billPurchaseProduct.getUnitVat())
                    .sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getTotalPriceAfterDiscountAndVat() {
        try {
            return (this.getTotalPrice() + this.getTotalVat()) - this.discount;
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public String getConditionInArabic() {
        try {
            return this.condition.getName();
        } catch (Exception ex) {
            return "";
        }
    }

}
