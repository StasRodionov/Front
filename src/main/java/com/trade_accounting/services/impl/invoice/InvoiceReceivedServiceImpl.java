package com.trade_accounting.services.impl.invoice;

import com.trade_accounting.models.dto.invoice.InvoiceReceivedDto;
import com.trade_accounting.services.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.invoice.InvoiceReceivedService;
import com.trade_accounting.services.api.invoice.InvoiceReceivedApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class InvoiceReceivedServiceImpl implements InvoiceReceivedService {

    private final InvoiceReceivedApi invoiceReceivedApi;

    private final String invoiceReceivedUrl;

    private final CallExecuteService<InvoiceReceivedDto> dtoCallExecuteService;

    public InvoiceReceivedServiceImpl(Retrofit retrofit, @Value("${invoice_received_url}") String invoiceReceivedUrl,
                                      CallExecuteService<InvoiceReceivedDto> dtoCallExecuteService) {
        this.invoiceReceivedApi = retrofit.create(InvoiceReceivedApi.class);
        this.invoiceReceivedUrl = invoiceReceivedUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<InvoiceReceivedDto> getAll() {
        Call<List<InvoiceReceivedDto>> listCall = invoiceReceivedApi.getAll(invoiceReceivedUrl);
        return dtoCallExecuteService.callExecuteBodyList(listCall, InvoiceReceivedDto.class);
    }

    @Override
    public InvoiceReceivedDto getById(Long id) {
        Call<InvoiceReceivedDto> invoiceReceivedDtoCall = invoiceReceivedApi.getById(invoiceReceivedUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(invoiceReceivedDtoCall, InvoiceReceivedDto.class, id);
    }

    @Override
    public void create(InvoiceReceivedDto invoiceReceivedDto) {
        Call<Void> voidCall = invoiceReceivedApi.create(invoiceReceivedUrl, invoiceReceivedDto);
        dtoCallExecuteService.callExecuteBodyCreate(voidCall, InvoiceReceivedDto.class);

    }

    @Override
    public void update(InvoiceReceivedDto invoiceReceivedDto) {
        Call<Void> voidCall = invoiceReceivedApi.update(invoiceReceivedUrl, invoiceReceivedDto);
        dtoCallExecuteService.callExecuteBodyUpdate(voidCall, InvoiceReceivedDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> voidCall = invoiceReceivedApi.deleteById(invoiceReceivedUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(voidCall, InvoiceReceivedDto.class, id);
    }

    @Override
    public List<InvoiceReceivedDto> searchByString(String search) {
        List<InvoiceReceivedDto> invoiceReceivedDtoListDtoList = new ArrayList<>();
        Call<List<InvoiceReceivedDto>> invoiceReceivedDtoCall = invoiceReceivedApi.searchByString(invoiceReceivedUrl, search);
        System.out.println(invoiceReceivedUrl+search);
        try {
            invoiceReceivedDtoListDtoList = invoiceReceivedDtoCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение счетов-фактур полученные по фильтру {}", search);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение счетов-фактур полученные {IOException}", e);
        }
        return invoiceReceivedDtoListDtoList;
    }
}
