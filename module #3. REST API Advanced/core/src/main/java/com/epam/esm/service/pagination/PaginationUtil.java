package com.epam.esm.service.pagination;

public class PaginationUtil {
    public static final int ON_PAGE = 10;

    public static int startValue(int pageNumber){
        return (pageNumber - 1) * ON_PAGE;
    }
}
