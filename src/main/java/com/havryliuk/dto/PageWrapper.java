package com.havryliuk.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Setter
public class PageWrapper<T> {

    public static final int MAX_PAGE_ITEM_DISPLAY = 5;
    public static final int INITIAL_PAGE_NUMBER = 1;
    public static final int DISPLAY_ITEMS_PER_PAGE = 4;

    private Page<T> pageItem;
    private List<Integer> pageNumbers;
    private List<Integer> maxItemsPerPage;

    private String requestURI;
    private int currentPage;
    private int size;

    private boolean isFirstPage;

    private boolean isLastPage;


    public PageWrapper(){
     }

    public PageWrapper(Page<T> page, int currentPageNo, int size, String requestURI){
        this.pageItem = page;
        this.currentPage = currentPageNo;
        this.size = size;
        this.requestURI = requestURI;
        this.maxItemsPerPage = putNumbersInList(1, MAX_PAGE_ITEM_DISPLAY);
        this.pageNumbers = putNumbersInList(1, page.getTotalPages());

    }
    private List<Integer> putNumbersInList(int from, int to) {
        return IntStream.rangeClosed(from, to)
                .boxed()
                .collect(Collectors.toList());
    }

    public boolean isFirstPage() {
        return currentPage == 1;
    }

    public boolean isLastPage() {
        return currentPage == pageNumbers.size();
    }

}
