package com.cas.spring.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * Created by tolga on 13.03.2016.
 */
@Entity
public class Cash {

    @Id
    @Column
    private String game;

    @Column
    private Double cash;

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public Double getCash() {
        return cash;
    }

    public void setCash(Double cash) {
        Double toBeTruncated = new Double(cash);

        Double truncatedDouble = new BigDecimal(toBeTruncated)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
        this.cash = truncatedDouble;
    }

}
