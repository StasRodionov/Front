package com.trade_accounting.components.apps.impl.util;

import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.models.dto.util.ColumnsMaskDto;
import com.trade_accounting.services.api.util.ColumnsMaskApi;
import com.trade_accounting.services.interfaces.util.ColumnsMaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

@Slf4j
@Service
public class ColumnsMaskServiceImpl implements ColumnsMaskService {

    private final ColumnsMaskApi columnsMaskApi;

    private final String columnsMaskUrl;

    private final CallExecuteService<ColumnsMaskDto> dtoCallExecuteService;

    public ColumnsMaskServiceImpl(@Value("${columns_mask_url}") String columnsMaskUrl, Retrofit retrofit,
                                  CallExecuteService<ColumnsMaskDto> dtoCallExecuteService) {
        this.columnsMaskUrl = columnsMaskUrl;
        this.columnsMaskApi = retrofit.create(ColumnsMaskApi.class);
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<ColumnsMaskDto> getAll() {
        Call<List<ColumnsMaskDto>> columnsMaskDtoListCall = columnsMaskApi.getAll(columnsMaskUrl);
        return dtoCallExecuteService.callExecuteBodyList(columnsMaskDtoListCall, ColumnsMaskDto.class);
    }

    @Override
    public ColumnsMaskDto getByGridId(int gridId) {
        ColumnsMaskDto columnsMaskDto = dtoCallExecuteService.callExecuteBody(
                columnsMaskApi.getById(columnsMaskUrl, gridId), ColumnsMaskDto.class);
        return columnsMaskDto == null || columnsMaskDto.getMask() == 0 ||
                columnsMaskDto.getGridDtoId() == 0 ?  new ColumnsMaskDto(gridId) : columnsMaskDto;
    }

    @Override
    public void create(ColumnsMaskDto columnsMaskDto) {
        dtoCallExecuteService.callExecuteBodyCreate(
                columnsMaskApi.create(columnsMaskUrl, columnsMaskDto), ColumnsMaskDto.class);
    }

    @Override
    public void update(ColumnsMaskDto columnsMaskDto) {
        dtoCallExecuteService.callExecuteBodyUpdate(
                columnsMaskApi.create(columnsMaskUrl, columnsMaskDto), ColumnsMaskDto.class);
    }

}
