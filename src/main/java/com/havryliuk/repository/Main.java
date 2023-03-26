package com.havryliuk.repository;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class Main {
    public static void main(String[] args) {
        OffsetDateTime odt = OffsetDateTime.parse("2020-12-20T00:00:00.000Z");
        System.out.println(odt);
        LocalDateTime ldt = odt.toLocalDateTime();
        System.out.println(ldt);
    }
}
