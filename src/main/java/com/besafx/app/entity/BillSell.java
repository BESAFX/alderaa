package com.besafx.app.entity;

import com.besafx.app.entity.enums.BillSellCondition;
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
public class BillSell implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "billSellSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "BILL_SELL_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "billSellSequenceGenerator")
    private Long id;

    private Long code;

    @Column(columnDefinition = "Decimal(10,1) default '0.0'")
    private Double discount;

    @Column(columnDefinition = "Decimal(10,1) default '0.0'")
    private Double transferFees;

    @ManyToOne
    @JoinColumn(name = "customer")
    private Customer customer;

    private String customerName;

    private String customerMobile;

    @Temporal(TemporalType.TIMESTAMP)
    private Date writtenDate;

    @Enumerated(EnumType.STRING)
    private BillSellCondition condition;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String note;

    @ManyToOne
    @JoinColumn(name = "person")
    private Person person;

    @OneToMany(mappedBy = "billSell")
    private List<BillSellAttach> billSellAttaches = new ArrayList<>();

    @OneToMany(mappedBy = "billSell")
    private List<BillSellProduct> billSellProducts = new ArrayList<>();

    @JsonCreator
    public static BillSell Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BillSell billSell = mapper.readValue(jsonString, BillSell.class);
        return billSell;
    }

    public Double getTotalPrice() {
        try {
            return this.billSellProducts
                    .stream()
                    .mapToDouble(billSellProduct -> billSellProduct.getQuantity() * billSellProduct.getUnitSellPrice())
                    .sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getTotalVat() {
        try {
            return this.billSellProducts
                    .stream()
                    .mapToDouble(billSellProduct -> billSellProduct.getQuantity() * billSellProduct.getUnitVat())
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
