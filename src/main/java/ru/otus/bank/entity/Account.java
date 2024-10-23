package ru.otus.bank.entity;

import java.math.BigDecimal;

public class Account {

    private long id;
    private BigDecimal amount;

    private Integer type;

    private String number;

    private Long agreementId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Long agreementId) {
        this.agreementId = agreementId;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", amount=" + amount +
                ", type=" + type +
                ", number='" + number + '\'' +
                ", agreementId=" + agreementId +
                "}\n";
    }
}
