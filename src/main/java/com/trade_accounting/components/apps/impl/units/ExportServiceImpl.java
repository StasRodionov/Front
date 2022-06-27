package com.trade_accounting.components.apps.impl.units;

import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.models.dto.units.ExportDto;
import com.trade_accounting.services.api.units.ExportApi;
import com.trade_accounting.services.interfaces.units.ExportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ExportServiceImpl implements ExportService {

    private final ExportApi exportApi;
    private final String exportUrl;
    private final CallExecuteService<ExportDto> exportDtoCallExecuteService;

    public ExportServiceImpl(@Value("${export_url}") String exportUrl, Retrofit retrofit, CallExecuteService<ExportDto> exportDtoCallExecuteService) {
        this.exportUrl = exportUrl;
        exportApi = retrofit.create(ExportApi.class);
        this.exportDtoCallExecuteService = exportDtoCallExecuteService;
    }

    @Override
    public List<ExportDto> getAll() {
        Call<List<ExportDto>> exportDtoGetAll = exportApi.getAll(exportUrl);
        return exportDtoCallExecuteService.callExecuteBodyList(exportDtoGetAll, ExportDto.class);
    }

    @Override
    public List<ExportDto> search(Map<String, String> query) {
        return null;
    }

    @Override
    public ExportDto getById(Long id) {
        return null;
    }

    @Override
    public void create(ExportDto scenarioDto) {

    }

    @Override
    public void update(ExportDto scenarioDto) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<ExportDto> findBySearch(String search) {
        return null;
    }
}