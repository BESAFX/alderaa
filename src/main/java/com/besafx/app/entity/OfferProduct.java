package com.besafx.app.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfferProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "offerProductSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "OFFER_PRODUCT_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "offerProductSequenceGenerator")
    private Long id;

    private Double quantity;

    private Double unitSellPrice;

    private Double unitVat;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "product")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "offer")
    private Offer offer;

    @JsonCreator
    public static OfferProduct Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        OfferProduct offerProduct = mapper.readValue(jsonString, OfferProduct.class);
        return offerProduct;
    }

    public Double getPrice() {
        try {
            return (this.quantity * this.unitSellPrice) + (this.quantity * this.unitVat);
        } catch (Exception ex) {
            return 0.0;
        }
    }
}
