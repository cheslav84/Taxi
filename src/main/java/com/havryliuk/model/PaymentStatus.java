package com.havryliuk.model;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    NOT_PAID("Not paid"),
    PAID("Paid");//todo rename enum

    public final String name;

    PaymentStatus(final String name) {
        this.name = name;
    }
}
