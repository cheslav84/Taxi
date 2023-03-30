package com.havryliuk.persistence.model;

import lombok.Getter;

@Getter
public enum CarClass {
    STANDARD("Standard"),
    BUSINESS("Business"),
    VIP("VIP"),
    BUS_MINIVAN("Bus-Minivan");

    public final String name;

    CarClass(final String name) {
        this.name = name;
    }

}
