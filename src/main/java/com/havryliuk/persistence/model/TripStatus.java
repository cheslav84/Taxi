package com.havryliuk.persistence.model;

public enum TripStatus {
    NEW,
    CONFIRMED,
    SEARCHING_CAR,// драйвер сканує всі поїздки з дамим статусом
    OFFERED,
    DRIVING,
    COMPLETED,
    CANCELED
}
