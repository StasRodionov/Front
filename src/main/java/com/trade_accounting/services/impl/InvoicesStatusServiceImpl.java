package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.InvoicesStatusDto;
import com.trade_accounting.services.interfaces.InvoicesStatusService;
import com.trade_accounting.services.interfaces.api.InvoicesStatusApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class InvoicesStatusServiceImpl implements InvoicesStatusService {

    private final InvoicesStatusApi invoicesStatusApi;

    private final String invoicesStatusUrl;

    private List<InvoicesStatusDto> InvoicesStatusDtoList = new ArrayList<>();

    private InvoicesStatusDto invoicesStatusDto = new InvoicesStatusDto();

    private final CallExecuteService<InvoicesStatusDto> dtoCallExecuteService;

    public InvoicesStatusServiceImpl(Retrofit retrofit, @Value("${invoices_status_url}")
            String invoicesStatusUrl, CallExecuteService<InvoicesStatusDto> dtoCallExecuteService) {

        invoicesStatusApi = retrofit.create(InvoicesStatusApi.class);
        this.invoicesStatusUrl = invoicesStatusUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }


    @Override
    public List<InvoicesStatusDto> getAll() {
        Call<List<InvoicesStatusDto>> statusGetAllCall = invoicesStatusApi.getAll(invoicesStatusUrl);
        return dtoCallExecuteService.callExecuteBodyList(statusGetAllCall, InvoicesStatusDto.class);
    }

    @Override
    public InvoicesStatusDto getById(Long id) {
        Call<InvoicesStatusDto> statusGetCall = invoicesStatusApi.getById(invoicesStatusUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(statusGetCall, invoicesStatusDto, InvoicesStatusDto.class, id);
    }

    @Override
    public void create(InvoicesStatusDto invoicesStatusDto) {
        Call<Void> statusCall = invoicesStatusApi.create(invoicesStatusUrl, invoicesStatusDto);
        dtoCallExecuteService.callExecuteBodyCreate(statusCall, InvoicesStatusDto.class);
    }

    @Override
    public void update(InvoicesStatusDto invoicesStatusDto) {
        Call<Void> statusUpdateCall = invoicesStatusApi.update(invoicesStatusUrl, invoicesStatusDto);
        dtoCallExecuteService.callExecuteBodyUpdate(statusUpdateCall, InvoicesStatusDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> statusDeleteCall = invoicesStatusApi.deleteById(invoicesStatusUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(statusDeleteCall, InvoicesStatusDto.class, id);
    }
}
