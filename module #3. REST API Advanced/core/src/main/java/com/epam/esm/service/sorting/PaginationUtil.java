package com.epam.esm.service.sorting;

public class PaginationUtil {
    public static final int ON_PAGE = 5;

    public static int startValue(int pageNumber){
        return (pageNumber - 1) * ON_PAGE;
    }
}
