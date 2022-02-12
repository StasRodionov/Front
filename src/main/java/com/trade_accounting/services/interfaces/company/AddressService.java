package com.trade_accounting.services.interfaces.company;

import com.trade_accounting.models.dto.company.AddressDto;

public interface AddressService {

    AddressDto getById(Long id);

    AddressDto create(AddressDto addressDto);

    void update(AddressDto addressDto);

    void deleteById(Long id);

}
