package com.trade_accounting.services.interfaces.util;

import com.trade_accounting.models.dto.util.ColumnsMaskDto;

import java.util.List;

public interface ColumnsMaskService {

    ColumnsMaskDto getByGridId(int gridId);

    List<ColumnsMaskDto> getAll();

    void update(ColumnsMaskDto columnsMaskDto);

    void create(ColumnsMaskDto columnsMaskDto);
}
