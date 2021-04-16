package com.trade_accounting.services.interfaces;

import java.util.List;
import java.util.Map;

public interface PageableService<T> {
    List<T> getPage(Map<String,String> filterParams, Map<String, String> sortParams, int page, int count);
    Long getRowsCount(Map<String, String> map);
}
