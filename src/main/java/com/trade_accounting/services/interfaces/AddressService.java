package com.trade_accounting.services.interfaces;

import com.trade_accounting.controllers.dto.AddressDto;

public interface AddressService {

    AddressDto getById(Long id);

    AddressDto create(AddressDto addressDto);

    void update(AddressDto addressDto);

    void deleteById(Long id);

}
