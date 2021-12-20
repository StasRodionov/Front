package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.InvoiceToBuyerListProductsDto;
import com.trade_accounting.services.interfaces.InvoiceToBuyerListProductsService;
import com.trade_accounting.services.interfaces.api.InvoiceToBuyerListProductsApi;
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
public class InvoiceToBuyerListProductsServiceImpl implements InvoiceToBuyerListProductsService {
    private final InvoiceToBuyerListProductsApi invoiceToBuyerListProductsApi;
    private final String invoiceToBuyerListProductsUrl;
    private final CallExecuteService<InvoiceToBuyerListProductsDto> dtoCallExecuteService;

    public InvoiceToBuyerListProductsServiceImpl(@Value("${invoice_to_buyer_list_products_url}") String invoiceToBuyerListProductsUrl, Retrofit retrofit, CallExecuteService<InvoiceToBuyerListProductsDto> dtoCallExecuteService){
        invoiceToBuyerListProductsApi = retrofit.create(InvoiceToBuyerListProductsApi.class);
        this.invoiceToBuyerListProductsUrl = invoiceToBuyerListProductsUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<InvoiceToBuyerListProductsDto> getAll() {
        Call<List<InvoiceToBuyerListProductsDto>> invoiceProductDtoListCall = invoiceToBuyerListProductsApi.getAll(invoiceToBuyerListProductsUrl);
        return dtoCallExecuteService.callExecuteBodyList(invoiceProductDtoListCall, InvoiceToBuyerListProductsDto.class);
    }

    @Override
    public InvoiceToBuyerListProductsDto getById(Long id) {
        Call<InvoiceToBuyerListProductsDto> invoiceProductDtoCall = invoiceToBuyerListProductsApi.getById(invoiceToBuyerListProductsUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(invoiceProductDtoCall, InvoiceToBuyerListProductsDto.class, id);
    }

    @Override
    public List<InvoiceToBuyerListProductsDto> getBySupplierId(Long id) {
        List<InvoiceToBuyerListProductsDto> invoiceToBuyerListProductsDtoList = null;
        Call<List<InvoiceToBuyerListProductsDto>> invoiceToBuyerListProductsListCall = invoiceToBuyerListProductsApi.getBySupplierId(invoiceToBuyerListProductsUrl + "/invoice_to_buyer_list_products", id);
        try{
            invoiceToBuyerListProductsDtoList = invoiceToBuyerListProductsListCall.execute().body();
            log.info("Успешно выполнен запрос на получение списка InvoiceToBuyerListProductsDto с SupplierAccountId.id = {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка InvoiceToBuyerListProductsDto по id= {} - {}", id, e);
        }
        return invoiceToBuyerListProductsDtoList;
    }

    @Override
    public void create(InvoiceToBuyerListProductsDto invoiceToBuyerListProductsDto) {
        Call<Void> invoiceProductDtoCall = invoiceToBuyerListProductsApi.create(invoiceToBuyerListProductsUrl, invoiceToBuyerListProductsDto);
        dtoCallExecuteService.callExecuteBodyCreate(invoiceProductDtoCall, InvoiceToBuyerListProductsDto.class);
    }

    @Override
    public void update(InvoiceToBuyerListProductsDto invoiceToBuyerListProductsDto) {
        Call<Void> invoiceProductDtoCall = invoiceToBuyerListProductsApi.create(invoiceToBuyerListProductsUrl, invoiceToBuyerListProductsDto);
        dtoCallExecuteService.callExecuteBodyUpdate(invoiceProductDtoCall, InvoiceToBuyerListProductsDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> invoiceProductDtoCall = invoiceToBuyerListProductsApi.deleteById(invoiceToBuyerListProductsUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(invoiceProductDtoCall,InvoiceToBuyerListProductsDto.class, id);
    }

    @Override
    public List<InvoiceToBuyerListProductsDto> search(Map<String, String> query) {
        List<InvoiceToBuyerListProductsDto> invoiceProductDtoList = null;
        Call<List<InvoiceToBuyerListProductsDto>> callDtoList = invoiceToBuyerListProductsApi.search(invoiceToBuyerListProductsUrl, query);
        try {
            invoiceProductDtoList = callDtoList.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка InvoiceToBuyerListProductsDto по ФИЛЬТРУ -{}", query);
        } catch (Exception e) {
            log.error("Произошла ошибка при выполнении запроса ФИЛЬТРА на поиск и получение списка InvoiceToBuyerListProductsDto - ");
        }
        return invoiceProductDtoList;
    }
}
