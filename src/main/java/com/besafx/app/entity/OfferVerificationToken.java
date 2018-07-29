package com.besafx.app.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class OfferVerificationToken implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(OfferVerificationToken.class);

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "offerVerificationTokenSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "OFFER_VERIFICATION_TOKEN_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "offerVerificationTokenSequenceGenerator")
    private Integer id;

    private String token;

    @OneToOne(targetEntity = Offer.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "offer")
    private Offer offer;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @JsonCreator
    public static OfferVerificationToken Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        OfferVerificationToken offerVerificationToken = mapper.readValue(jsonString, OfferVerificationToken.class);
        return offerVerificationToken;
    }
}
