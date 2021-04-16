package com.trade_accounting.services.interfaces;

import com.vaadin.flow.function.SerializableComparator;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Null;
import java.util.List;
import java.util.Map;

public interface PaginatorInterface<T> {
    List<T> getList(Map<String, String> sortParams, int page, int count);
    //TODO потенциально удалить, так как мапа у фильтра пустая
    Long getRowCount();
    Long getRowCount(Map<String, String> map);
    List<T> getListFilter(Map<String,String> map, Map<String, String> sortParams, int page, int count);
    List<T> getListFilterComparator(SerializableComparator<T> comparator, int page, int count);
}
