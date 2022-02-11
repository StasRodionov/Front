package com.trade_accounting.services.interfaces.company;

import com.trade_accounting.models.dto.company.ContactDto;

import java.util.List;

public interface ContactService {

    List<ContactDto> getAll();

    ContactDto getById(Long id);

    ContactDto create(ContactDto contactDto);

    void update(ContactDto contactDto);

    void deleteById(Long id);
}
