package com.trade_accounting.services.interfaces.units;

import com.trade_accounting.models.dto.units.SalesChannelDto;

import java.util.List;
import java.util.Map;

public interface SalesChannelService {

    List<SalesChannelDto> getAll();

    SalesChannelDto getById(Long id);

    void create(SalesChannelDto dto);

    void update(SalesChannelDto dto);

    void deleteById(Long id);
    List<SalesChannelDto> search(Map<String, String> query);
    List<SalesChannelDto> searchByString(String search);
}
