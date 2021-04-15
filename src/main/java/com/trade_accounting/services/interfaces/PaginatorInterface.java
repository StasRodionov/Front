package com.trade_accounting.services.interfaces;

import com.vaadin.flow.function.SerializableComparator;

import java.util.List;
import java.util.Map;

public interface PaginatorInterface<T> {
    List<T> getList(int page, int count);
    Long getRowCount();
    Long getRowCount(Map<String, String> map);
    List<T> getListFilter(Map<String,String> map, int page, int count);
    List<T> getListFilterComparator(SerializableComparator<T> comparator, int page, int count);
}
