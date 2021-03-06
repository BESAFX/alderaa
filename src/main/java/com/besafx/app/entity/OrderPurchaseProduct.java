package com.besafx.app.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderPurchaseProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "orderPurchaseProductSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "ORDER_PURCHASE_PRODUCT_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "orderPurchaseProductSequenceGenerator")
    private Long id;

    private Double quantity;

    private Double unitPurchasePrice;

    private Double unitVat;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "product")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "orderPurchase")
    private OrderPurchase orderPurchase;

    @JsonCreator
    public static OrderPurchaseProduct Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        OrderPurchaseProduct orderPurchaseProduct = mapper.readValue(jsonString, OrderPurchaseProduct.class);
        return orderPurchaseProduct;
    }

    public Double getPrice() {
        try {
            return (this.quantity * this.unitPurchasePrice) + (this.quantity * this.unitVat);
        } catch (Exception ex) {
            return 0.0;
        }
    }
}
