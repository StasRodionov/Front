package com.trade_accounting.components.apps.impl.production;

import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.models.dto.production.ProductionTargetsDto;
import com.trade_accounting.services.interfaces.production.ProductionTargetsService;
import com.trade_accounting.services.api.production.ProductionTargetsApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class ProductionTargetsServiceImpl implements ProductionTargetsService {

    private final String productionTargetsUrl;
    private final ProductionTargetsApi productionTargetsApi;
    private final CallExecuteService<ProductionTargetsDto> dtoCallExecuteService;
    private List<ProductionTargetsDto> productionTargetsDtoList = new ArrayList<>();
    private final ProductionTargetsDto productionTargetsDto = new ProductionTargetsDto();


    public ProductionTargetsServiceImpl(@Value("${production_targets_url}") String productionTargetsUrl,
                                        Retrofit retrofit, CallExecuteService<ProductionTargetsDto> dtoCallExecuteService) {

        this.productionTargetsUrl = productionTargetsUrl;
        this.productionTargetsApi = retrofit.create(ProductionTargetsApi.class);
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<ProductionTargetsDto> getAll() {
        Call<List<ProductionTargetsDto>> productionTargetsGetAllCall = productionTargetsApi.getAll(productionTargetsUrl);
        return dtoCallExecuteService.callExecuteBodyList(productionTargetsGetAllCall, ProductionTargetsDto.class);
    }

    @Override
    public ProductionTargetsDto getById(Long id) {
        Call<ProductionTargetsDto> productionTargetsGetCall = productionTargetsApi.getById(productionTargetsUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(productionTargetsGetCall, ProductionTargetsDto.class, id);
    }

    @Override
    public void create(ProductionTargetsDto dto) {
        Call<Void> productionTargetsCreateCall = productionTargetsApi.create(productionTargetsUrl, dto);
        dtoCallExecuteService.callExecuteBodyCreate(productionTargetsCreateCall, ProductionTargetsDto.class);
    }

    @Override
    public void update(ProductionTargetsDto dto) {
        Call<Void> productionTargetsUpdateCall = productionTargetsApi.update(productionTargetsUrl, dto);
        dtoCallExecuteService.callExecuteBodyUpdate(productionTargetsUpdateCall, ProductionTargetsDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> productionTargetsDeleteCall = productionTargetsApi.deleteById(productionTargetsUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(productionTargetsDeleteCall, ProductionTargetsDto.class, id);
    }

    @Override
    public List<ProductionTargetsDto> searchProductionTargets(Map<String, String> queryProductionTargets) {
        List<ProductionTargetsDto> productionTargetsList = new ArrayList<>();
        Call<List<ProductionTargetsDto>> productionTargetsDtoListCall
                = productionTargetsApi.searchProductionTargets(productionTargetsUrl, queryProductionTargets);

        try {
            productionTargetsList = productionTargetsDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка производственных заданий");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка производственных заданий: {IOException}", e);
        }
        return productionTargetsList;
    }

    @Override
    public List<ProductionTargetsDto> search(String query) {
        Call<List<ProductionTargetsDto>> productionTargetsDtoListCall = productionTargetsApi.search(productionTargetsUrl, query);
        try {
            productionTargetsDtoList = productionTargetsDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка ProductionTargetsDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка ProductionTargetsDto - ", e);
        }
        return productionTargetsDtoList;
    }
}
