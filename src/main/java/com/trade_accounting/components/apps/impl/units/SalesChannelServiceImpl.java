package com.trade_accounting.components.apps.impl.units;

import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.models.dto.units.SalesChannelDto;
import com.trade_accounting.services.api.units.SalesChannelApi;
import com.trade_accounting.services.interfaces.units.SalesChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

@Service
@Slf4j
public class SalesChannelServiceImpl implements SalesChannelService {

    private final SalesChannelApi salesChannelApi;

    private final String salesChannelUrl;

    private final CallExecuteService<SalesChannelDto> dtoCallExecuteService;

    public SalesChannelServiceImpl(@Value("${sales_channel_url}") String salesChannelUrl, Retrofit retrofit, CallExecuteService<SalesChannelDto> dtoCallExecuteService) {
        this.salesChannelUrl = salesChannelUrl;
        salesChannelApi = retrofit.create(SalesChannelApi.class);
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<SalesChannelDto> getAll() {
        Call<List<SalesChannelDto>> salesChannelListDtoCall = salesChannelApi.getAll(salesChannelUrl);
        return dtoCallExecuteService.callExecuteBodyList(salesChannelListDtoCall, SalesChannelDto.class);
    }

    @Override
    public SalesChannelDto getById(Long id) {
        Call<SalesChannelDto> salesChannelDtoCall = salesChannelApi.getById(salesChannelUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(salesChannelDtoCall, SalesChannelDto.class, id);
    }

    @Override
    public void create(SalesChannelDto dto) {
        Call<Void> salesChannelDtoCall = salesChannelApi.create(salesChannelUrl, dto);
        dtoCallExecuteService.callExecuteBodyCreate(salesChannelDtoCall, SalesChannelDto.class);
    }

    @Override
    public void update(SalesChannelDto dto) {
        Call<Void> salesChannelDtoCall = salesChannelApi.update(salesChannelUrl, dto);
        dtoCallExecuteService.callExecuteBodyUpdate(salesChannelDtoCall, SalesChannelDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> salesChannelDtoCall = salesChannelApi.deleteById(salesChannelUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(salesChannelDtoCall, SalesChannelDto.class, id);
    }
}
