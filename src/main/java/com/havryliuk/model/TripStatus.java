package com.havryliuk.model;

public enum TripStatus {
    NEW,
    SEARCHING_CAR,// драйвер сканує всі поїздки з дамим статусом
    OFFERED,
    DRIVING,
    COMPLETED,
    CANCELED
}
