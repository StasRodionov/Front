package com.trade_accounting.services.impl.warehouse;

import com.trade_accounting.models.dto.warehouse.AttributeOfCalculationObjectDto;
import com.trade_accounting.services.impl.CallExecuteService;
import com.trade_accounting.services.interfaces.warehouse.AttributeOfCalculationObjectService;
import com.trade_accounting.services.api.warehouse.AttributeOfCalculationObjectApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;


@Slf4j
@Service
public class AttributeOfCalculationObjectServiceImpl implements AttributeOfCalculationObjectService {

    private final AttributeOfCalculationObjectApi attributeOfCalculationObjectApi;

    private final String attributeOfCalculationObjectUrl;

    private final CallExecuteService<AttributeOfCalculationObjectDto> dtoCallExecuteService;

    @Autowired
    public AttributeOfCalculationObjectServiceImpl(@Value("${attribute_calculation_object_url}") String attributeOfCalculationObjectUrl, Retrofit retrofit, CallExecuteService<AttributeOfCalculationObjectDto> dtoCallExecuteService) {
        attributeOfCalculationObjectApi = retrofit.create(AttributeOfCalculationObjectApi.class);
        this.attributeOfCalculationObjectUrl = attributeOfCalculationObjectUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<AttributeOfCalculationObjectDto> getAll() {

        Call<List<AttributeOfCalculationObjectDto>> attributeOfCalculationObjectDtoListCall = attributeOfCalculationObjectApi.getAll(attributeOfCalculationObjectUrl);
        return dtoCallExecuteService.callExecuteBodyList(attributeOfCalculationObjectDtoListCall, AttributeOfCalculationObjectDto.class);

    }

    @Override
    public AttributeOfCalculationObjectDto getById(Long id) {
        Call<AttributeOfCalculationObjectDto> attributeOfCalculationObjectDtoCall = attributeOfCalculationObjectApi.getById(attributeOfCalculationObjectUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(attributeOfCalculationObjectDtoCall,
                AttributeOfCalculationObjectDto.class, id);
    }

    @Override
    public void create(AttributeOfCalculationObjectDto attributeOfCalculationObjectDto) {
        Call<Void> attributeOfCalculationObjectDtoCall = attributeOfCalculationObjectApi.create(attributeOfCalculationObjectUrl, attributeOfCalculationObjectDto);
        dtoCallExecuteService.callExecuteBodyCreate(attributeOfCalculationObjectDtoCall, AttributeOfCalculationObjectDto.class);

    }

    @Override
    public void update(AttributeOfCalculationObjectDto attributeOfCalculationObjectDto) {
        Call<Void> attributeOfCalculationObjectDtoCall = attributeOfCalculationObjectApi.update(attributeOfCalculationObjectUrl, attributeOfCalculationObjectDto);
        dtoCallExecuteService.callExecuteBodyUpdate(attributeOfCalculationObjectDtoCall, AttributeOfCalculationObjectDto.class);

    }

    @Override
    public void deleteById(Long id) {
        Call<Void> attributeOfCalculationObjectDtoCall = attributeOfCalculationObjectApi.deleteById(attributeOfCalculationObjectUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(attributeOfCalculationObjectDtoCall, AttributeOfCalculationObjectDto.class, id);

    }

}
