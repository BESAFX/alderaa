package com.besafx.app.entity;

import com.besafx.app.entity.enums.OfferCondition;
import com.besafx.app.entity.enums.OrderSellCondition;
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
public class OrderSell implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "orderSellSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "ORDER_SELL_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "orderSellSequenceGenerator")
    private Long id;

    private Long code;

    private Double discount;

    private Double transferFees;

    @Enumerated(EnumType.STRING)
    private OrderSellCondition condition;

    @ManyToOne
    @JoinColumn(name = "customer")
    private Customer customer;

    @Temporal(TemporalType.TIMESTAMP)
    private Date writtenDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String rules;

    @ManyToOne
    @JoinColumn(name = "person")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "billSell")
    private BillSell billSell;

    @OneToMany(mappedBy = "orderSell")
    private List<OrderSellProduct> orderSellProducts = new ArrayList<>();

    @JsonCreator
    public static OrderSell Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        OrderSell order = mapper.readValue(jsonString, OrderSell.class);
        return order;
    }

    public Double getTotalPrice() {
        try {
            return this.orderSellProducts
                    .stream()
                    .mapToDouble(orderProduct -> orderProduct.getQuantity() * orderProduct.getUnitSellPrice())
                    .sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getTotalVat() {
        try {
            return this.orderSellProducts
                    .stream()
                    .mapToDouble(orderProduct -> orderProduct.getQuantity() * orderProduct.getUnitVat())
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
