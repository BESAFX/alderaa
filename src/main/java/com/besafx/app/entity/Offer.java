package com.besafx.app.entity;

import com.besafx.app.entity.enums.OfferCondition;
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
public class Offer implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "offerSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "OFFER_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "offerSequenceGenerator")
    private Long id;

    private Long code;

    private Double discount;

    private Double transferFees;

    @ManyToOne
    @JoinColumn(name = "customer")
    private Customer customer;

    @Temporal(TemporalType.TIMESTAMP)
    private Date writtenDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private OfferCondition condition;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String note;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String rules;

    @ManyToOne
    @JoinColumn(name = "person")
    private Person person;

    @OneToMany(mappedBy = "offer")
    private List<OfferAttach> offerAttaches = new ArrayList<>();

    @OneToMany(mappedBy = "offer")
    private List<OfferProduct> offerProducts = new ArrayList<>();

    @JsonCreator
    public static Offer Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Offer offer = mapper.readValue(jsonString, Offer.class);
        return offer;
    }

    public Double getTotalPrice() {
        try {
            return this.offerProducts
                    .stream()
                    .mapToDouble(offerProduct -> offerProduct.getQuantity() * offerProduct.getUnitSellPrice())
                    .sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getTotalVat() {
        try {
            return this.offerProducts
                    .stream()
                    .mapToDouble(offerProduct -> offerProduct.getQuantity() * offerProduct.getUnitVat())
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
