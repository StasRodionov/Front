package com.trade_accounting.services.interfaces.company;

import com.trade_accounting.models.dto.company.BankAccountDto;

import java.util.List;

public interface BankAccountService {

    List<BankAccountDto> getAll();

    BankAccountDto getById(Long id);

    BankAccountDto create(BankAccountDto dto);

    void update(BankAccountDto dto);

    void deleteById(Long id);
}
