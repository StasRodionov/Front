package com.trade_accounting.services.interfaces.company;

import com.trade_accounting.models.dto.company.TypeOfContractorDto;

import java.util.List;

public interface TypeOfContractorService {

    List<TypeOfContractorDto> getAll();

    TypeOfContractorDto getById(Long id);

    void create(TypeOfContractorDto typeOfContractorDto);

    void update(TypeOfContractorDto typeOfContractorDto);

    void deleteById(Long id);
}
