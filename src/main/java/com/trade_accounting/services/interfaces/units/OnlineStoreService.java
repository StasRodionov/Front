package com.trade_accounting.services.interfaces.units;

import com.trade_accounting.models.dto.units.OnlineStoreDto;

import java.util.List;
import java.util.Map;

public interface OnlineStoreService {

    List<OnlineStoreDto> getAll();
    OnlineStoreDto getById(Long id);
    void create(OnlineStoreDto onlineStoreDto);
    void update(OnlineStoreDto onlineStoreDto);
    void deleteById(Long id);
    List<OnlineStoreDto> search(Map<String, String> searchQuery);
    List<OnlineStoreDto> searchByString(String search);
}
