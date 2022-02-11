package com.trade_accounting.services.interfaces.util;

import com.trade_accounting.models.dto.util.PageDto;

import java.util.Map;

public interface PageableService<T> {
    PageDto<T> getPage(Map<String,String> filterParams, Map<String, String> sortParams, int page, int count);
}
