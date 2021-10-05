package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.IssuedInvoiceDto;
import com.trade_accounting.services.interfaces.IssuedInvoiceService;
import com.trade_accounting.services.interfaces.api.IssuedInvoiceApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

@Service
@Slf4j
public class IssuedInvoiceServiceImpl implements IssuedInvoiceService {

    private final IssuedInvoiceApi issuedInvoiceApi;

    private final String issuedInvoiceUrl;

    private IssuedInvoiceDto issuedInvoiceDto;

    private final CallExecuteService<IssuedInvoiceDto> dtoCallExecuteService;

    public IssuedInvoiceServiceImpl(Retrofit retrofit, @Value("${issued_invoice_url}") String issuedInvoiceUrl, CallExecuteService<IssuedInvoiceDto> dtoCallExecuteService) {
        this.issuedInvoiceApi = retrofit.create(IssuedInvoiceApi.class);
        this.issuedInvoiceUrl = issuedInvoiceUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<IssuedInvoiceDto> getAll() {
        Call<List<IssuedInvoiceDto>> issuedInvoiceApiAll = issuedInvoiceApi.getAll(issuedInvoiceUrl);
        return dtoCallExecuteService.callExecuteBodyList(issuedInvoiceApiAll, IssuedInvoiceDto.class);
    }

    @Override
    public IssuedInvoiceDto getById(Long id) {
        Call<IssuedInvoiceDto> issuedInvoiceDtoCall = issuedInvoiceApi.getById(issuedInvoiceUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(issuedInvoiceDtoCall, issuedInvoiceDto, IssuedInvoiceDto.class, id);
    }

    @Override
    public void create(IssuedInvoiceDto issuedInvoiceDto) {
        Call<Void> issuedInvoiceDtoCall = issuedInvoiceApi.create(issuedInvoiceUrl, issuedInvoiceDto);
        dtoCallExecuteService.callExecuteBodyCreate(issuedInvoiceDtoCall, IssuedInvoiceDto.class);

    }

    @Override
    public void update(IssuedInvoiceDto issuedInvoiceDto) {
        Call<Void> issuedInvoiceDtoCall = issuedInvoiceApi.update(issuedInvoiceUrl, issuedInvoiceDto);
        dtoCallExecuteService.callExecuteBodyUpdate(issuedInvoiceDtoCall, IssuedInvoiceDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> issuedInvoiceDtoCall = issuedInvoiceApi.deleteById(issuedInvoiceUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(issuedInvoiceDtoCall, IssuedInvoiceDto.class, id);
    }
}
