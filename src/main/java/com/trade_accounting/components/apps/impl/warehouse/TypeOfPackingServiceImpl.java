package com.trade_accounting.components.apps.impl.warehouse;


import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.models.dto.warehouse.TypeOfPackingDto;
import com.trade_accounting.services.api.warehouse.TypeOfPackingApi;
import com.trade_accounting.services.interfaces.warehouse.TypeOfPackingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

@Slf4j
@Service
public class TypeOfPackingServiceImpl implements TypeOfPackingService {

    private final CallExecuteService<TypeOfPackingDto> dtoCallExecuteService;

    private final String typeOfPackingUrl;

    private final TypeOfPackingApi typeOfPackingApi;


    public TypeOfPackingServiceImpl(@Value("${type_of_packing_url}") String typeOfPackingUrl,
                                    Retrofit retrofit,
                                    CallExecuteService<TypeOfPackingDto> dtoCallExecuteService) {
        this.typeOfPackingUrl = typeOfPackingUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
        typeOfPackingApi = retrofit.create(TypeOfPackingApi.class);
    }


    @Override
    public List<TypeOfPackingDto> getAll() {
        Call<List<TypeOfPackingDto>> typeOfPackingDtoListCall = typeOfPackingApi.getAll(typeOfPackingUrl);
        return dtoCallExecuteService.callExecuteBodyList(typeOfPackingDtoListCall, TypeOfPackingDto.class);
    }
}
