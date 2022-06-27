package com.trade_accounting.components.apps.impl.util;

import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.models.dto.util.DiscountDto;
import com.trade_accounting.services.api.util.DiscountApi;
import com.trade_accounting.services.interfaces.util.DiscountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

@Slf4j
@Service
public class DiscountServiceImpl implements DiscountService {

    private final DiscountApi discountApi;
    private final String discountUrl;
    private final CallExecuteService<DiscountDto> callExecuteService;

    public DiscountServiceImpl(@Value("${discount_url}") String discountUrl, CallExecuteService<DiscountDto> callExecuteService, Retrofit retrofit) {
        this.discountUrl = discountUrl;
        this.callExecuteService = callExecuteService;
        discountApi = retrofit.create(DiscountApi.class);
    }

    @Override
    public List<DiscountDto> getAll() {
        Call<List<DiscountDto>> listCall = discountApi.getAll(discountUrl);
        return callExecuteService.callExecuteBodyList(listCall, DiscountDto.class);
    }

    @Override
    public DiscountDto getByID(Long id) {
        Call<DiscountDto> call = discountApi.getById(discountUrl, id);
        return callExecuteService.callExecuteBodyById(call, DiscountDto.class, id);
    }

    @Override
    public void create(DiscountDto discountDto) {
        Call<Void> call = discountApi.create(discountUrl, discountDto);
        callExecuteService.callExecuteBodyCreate(call, DiscountDto.class);
    }

    @Override
    public void update(DiscountDto discountDto) {
        Call<Void> call = discountApi.update(discountUrl, discountDto);
        callExecuteService.callExecuteBodyUpdate(call, DiscountDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> call = discountApi.deleteById(discountUrl, id);
        callExecuteService.callExecuteBodyDelete(call, DiscountDto.class, id);
    }
}
