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
public class OrderSellProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "orderSellProductSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "ORDER_SELL_PRODUCT_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "orderSellProductSequenceGenerator")
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
    @JoinColumn(name = "orderSell")
    private OrderSell orderSell;

    @JsonCreator
    public static OrderSellProduct Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        OrderSellProduct orderSellProduct = mapper.readValue(jsonString, OrderSellProduct.class);
        return orderSellProduct;
    }

    public Double getPrice() {
        try {
            return (this.quantity * this.unitSellPrice) + (this.quantity * this.unitVat);
        } catch (Exception ex) {
            return 0.0;
        }
    }
}
