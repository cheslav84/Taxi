package com.havryliuk.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;


@Getter
@Setter
public class SortWrapper {

    private final String sortField;
    private final String sortDirection;
    private final String reverseSortDirection;
    private final Sort.Direction direction;
    private final Sort sortOrders;

    public SortWrapper(String[] sorting) {
        this.sortField = sorting[0];
        this.sortDirection = sorting[1];
        this.reverseSortDirection = sortDirection.equals("asc") ? "desc" : "asc";
        this.direction = sortDirection.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        this.sortOrders = Sort.by(direction, sortField);
    }

}
