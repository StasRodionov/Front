package com.trade_accounting.components.apps.impl.invoice;

import com.trade_accounting.models.dto.invoice.InvoiceProductDto;
import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.invoice.InvoiceProductService;
import com.trade_accounting.services.api.invoice.InvoiceProductApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductApi invoiceProductApi;

    private final String invoiceProductUrl;

    private final CallExecuteService<InvoiceProductDto> dtoCallExecuteService;

    public InvoiceProductServiceImpl(@Value("${invoice_product_url}") String invoiceProductUrl, Retrofit retrofit, CallExecuteService<InvoiceProductDto> dtoCallExecuteService){
        invoiceProductApi = retrofit.create(InvoiceProductApi.class);
        this.invoiceProductUrl = invoiceProductUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<InvoiceProductDto> getAll() {
        Call<List<InvoiceProductDto>> invoiceProductDtoListCall = invoiceProductApi.getAll(invoiceProductUrl);
        return dtoCallExecuteService.callExecuteBodyList(invoiceProductDtoListCall, InvoiceProductDto.class);
    }

    @Override
    public InvoiceProductDto getById(Long id) {
        Call<InvoiceProductDto> invoiceProductDtoCall = invoiceProductApi.getById(invoiceProductUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(invoiceProductDtoCall, InvoiceProductDto.class, id);
    }

    @Override
    public List<InvoiceProductDto> getByInvoiceId(Long id) {
        List<InvoiceProductDto> invoiceProductDtoList = null;
        Call<List<InvoiceProductDto>> invoiceProductDtoCall = invoiceProductApi.getByInvoiceId(invoiceProductUrl + "/invoice_product", id);
        try{
            invoiceProductDtoList = invoiceProductDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка InvoiceProductDto с Invoice.id = {}", id);
        } catch (IOException e){
            log.error("Произошла ошибка при выполнении запроса на получение списка InvoiceProductDto по id= {} - {}", id, e);
        }
        return invoiceProductDtoList;
    }

    @Override
    public void create(InvoiceProductDto invoiceProductDto) {
        Call<Void> invoiceProductDtoCall = invoiceProductApi.create(invoiceProductUrl, invoiceProductDto);
        dtoCallExecuteService.callExecuteBodyCreate(invoiceProductDtoCall, InvoiceProductDto.class);
    }

    @Override
    public void update(InvoiceProductDto invoiceProductDto) {
        Call<Void> invoiceProductDtoCall = invoiceProductApi.create(invoiceProductUrl, invoiceProductDto);
        dtoCallExecuteService.callExecuteBodyUpdate(invoiceProductDtoCall, InvoiceProductDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> invoiceProductDtoCall = invoiceProductApi.deleteById(invoiceProductUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(invoiceProductDtoCall,InvoiceProductDto.class, id);
    }

    @Override
    public List<InvoiceProductDto> search(Map<String, String> query) {
        List<InvoiceProductDto> invoiceProductDtoList = null;
        Call<List<InvoiceProductDto>> callDtoList = invoiceProductApi.search(invoiceProductUrl, query);
        try {
            invoiceProductDtoList = callDtoList.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка InvoiceProductDto по ФИЛЬТРУ -{}", query);
        } catch (Exception e) {
            log.error("Произошла ошибка при выполнении запроса ФИЛЬТРА на поиск и получение списка InvoiceProductDto - ");
        }
        return invoiceProductDtoList;
    }

    @Override
    public List<InvoiceProductDto> getByProductId(Long id) {
        List<InvoiceProductDto> invoiceProductDtoList = null;
        Call<List<InvoiceProductDto>> invoiceProductDtoCall = invoiceProductApi.getByProductId(invoiceProductUrl, id);
        try{
            invoiceProductDtoList = invoiceProductDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка InvoiceProductDto с productId = {}", id);
        } catch (IOException e){
            log.error("Произошла ошибка при выполнении запроса на получение списка InvoiceProductDto по productId= {} - {}", id, e);
        }
        return invoiceProductDtoList;
    }
}
