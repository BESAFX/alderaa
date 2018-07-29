package com.besafx.app.entity;

import com.besafx.app.entity.enums.OrderPurchaseCondition;
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
public class OrderPurchase implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "orderPurchaseSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "ORDER_PURCHASE_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "orderPurchaseSequenceGenerator")
    private Long id;

    private Long code;

    private Double discount;

    private Double transferFees;

    @Enumerated(EnumType.STRING)
    private OrderPurchaseCondition condition;

    @ManyToOne
    @JoinColumn(name = "supplier")
    private Supplier supplier;

    @Temporal(TemporalType.TIMESTAMP)
    private Date writtenDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryDate;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String note;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String rules;

    @ManyToOne
    @JoinColumn(name = "person")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "billPurchase")
    private BillPurchase billPurchase;

    @OneToMany(mappedBy = "orderPurchase")
    private List<OrderPurchaseProduct> orderPurchaseProducts = new ArrayList<>();

    @JsonCreator
    public static OrderPurchase Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        OrderPurchase order = mapper.readValue(jsonString, OrderPurchase.class);
        return order;
    }

    public Double getTotalPrice() {
        try {
            return this.orderPurchaseProducts
                    .stream()
                    .mapToDouble(orderProduct -> orderProduct.getQuantity() * orderProduct.getUnitPurchasePrice())
                    .sum();
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public Double getTotalVat() {
        try {
            return this.orderPurchaseProducts
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
