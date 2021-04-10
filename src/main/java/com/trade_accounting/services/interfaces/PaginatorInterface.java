package com.trade_accounting.services.interfaces;

import java.util.List;
import java.util.Map;

public interface PaginatorInterface<T> {
    List<T> getList(int page, int count);
    Long getRowCount();
//    List<T> getList(Map<String,String> map, int page, int count);
}
