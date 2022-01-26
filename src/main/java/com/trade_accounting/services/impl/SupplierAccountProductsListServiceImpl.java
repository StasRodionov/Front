package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.SupplierAccountProductsListDto;
import com.trade_accounting.services.interfaces.SupplierAccountProductsListService;
import com.trade_accounting.services.interfaces.api.SupplierAccountProductsListApi;
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
public class SupplierAccountProductsListServiceImpl implements SupplierAccountProductsListService {
    private final SupplierAccountProductsListApi supplierAccountProductsListApi;
    private final String supplierAccountProductsListUrl;
    private final CallExecuteService<SupplierAccountProductsListDto> dtoCallExecuteService;

    public SupplierAccountProductsListServiceImpl(@Value("${supplier_account_products_list_url}") String supplierAccountProductsListUrl, Retrofit retrofit, CallExecuteService<SupplierAccountProductsListDto> dtoCallExecuteService){
        supplierAccountProductsListApi = retrofit.create(SupplierAccountProductsListApi.class);
        this.supplierAccountProductsListUrl = supplierAccountProductsListUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<SupplierAccountProductsListDto> getAll() {
        Call<List<SupplierAccountProductsListDto>> supplierAccountProductsListCall = supplierAccountProductsListApi.getAll(supplierAccountProductsListUrl);
        return dtoCallExecuteService.callExecuteBodyList(supplierAccountProductsListCall, SupplierAccountProductsListDto.class);
    }

    @Override
    public SupplierAccountProductsListDto getById(Long id) {
        Call<SupplierAccountProductsListDto> supplierAccountProductsListDtoCall = supplierAccountProductsListApi.getById(supplierAccountProductsListUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(supplierAccountProductsListDtoCall, SupplierAccountProductsListDto.class, id);
    }

    @Override
    public List<SupplierAccountProductsListDto> getBySupplierId(Long id) {
        List<SupplierAccountProductsListDto> supplierAccountProductsListDtos = null;
        Call<List<SupplierAccountProductsListDto>>  supplierAccountProductsListApiBySupplierId = supplierAccountProductsListApi.getBySupplierId(supplierAccountProductsListUrl + "/supplier-account-products-list", id);
        try{
            supplierAccountProductsListDtos = supplierAccountProductsListApiBySupplierId.execute().body();
            log.info("Успешно выполнен запрос на получение списка InvoiceToBuyerListProductsDto с SupplierAccountId.id = {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение списка InvoiceToBuyerListProductsDto по id= {} - {}", id, e);
        }
        return supplierAccountProductsListDtos;
    }

    @Override
    public void create(SupplierAccountProductsListDto supplierAccountProductsListDto) {
        Call<Void> supplierAccountProductsListCall = supplierAccountProductsListApi.create(supplierAccountProductsListUrl, supplierAccountProductsListDto);
        dtoCallExecuteService.callExecuteBodyCreate(supplierAccountProductsListCall, SupplierAccountProductsListDto.class);
    }

    @Override
    public void update(SupplierAccountProductsListDto supplierAccountProductsListDto) {
        Call<Void> SupplierAccountProductsListCall = supplierAccountProductsListApi.create(supplierAccountProductsListUrl, supplierAccountProductsListDto);
        dtoCallExecuteService.callExecuteBodyUpdate(SupplierAccountProductsListCall, SupplierAccountProductsListDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> supplierAccountProductsListCall = supplierAccountProductsListApi.deleteById(supplierAccountProductsListUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(supplierAccountProductsListCall,SupplierAccountProductsListDto.class, id);
    }

    @Override
    public List<SupplierAccountProductsListDto> search(Map<String, String> query) {
        List<SupplierAccountProductsListDto> supplierAccountProductsList = null;
        Call<List<SupplierAccountProductsListDto>> callDtoList = supplierAccountProductsListApi.search(supplierAccountProductsListUrl, query);
        try {
            supplierAccountProductsList = callDtoList.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка InvoiceToBuyerListProductsDto по ФИЛЬТРУ -{}", query);
        } catch (Exception e) {
            log.error("Произошла ошибка при выполнении запроса ФИЛЬТРА на поиск и получение списка InvoiceToBuyerListProductsDto - ");
        }
        return supplierAccountProductsList;
    }
}
