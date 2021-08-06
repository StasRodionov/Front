package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.models.dto.MovementDto;

import java.util.List;
import java.util.Map;

public interface MovementService {
    List<MovementDto> getAll();

    List<MovementDto> getAll(String typeOfInvoice);

    MovementDto getById(Long id);

    MovementDto create(MovementDto movementDto);

    void update(MovementDto movementDto);

    void deleteById(Long id);

    List<MovementDto> search(Map<String, String> query);

}
