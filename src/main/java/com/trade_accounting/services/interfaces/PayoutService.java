package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.PayoutDto;

import java.util.List;
import java.util.Map;

public interface PayoutService {

    List<PayoutDto> getAll();

    List<PayoutDto> getAllByParameters(String searchTerm);

    List<PayoutDto> searchByFilter(Map<String, String> query);

    PayoutDto getById(Long id);

    void create(PayoutDto payoutDto);

    void update(PayoutDto payoutDto);

    void deleteById(Long id);

    List<PayoutDto> findBySearchAndTypeOfPayout(String search, String typeOfPayout);


}
