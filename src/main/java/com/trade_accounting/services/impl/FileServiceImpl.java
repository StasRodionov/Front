package com.trade_accounting.services.impl;

import com.trade_accounting.controllers.dto.FileDto;
import com.trade_accounting.services.interfaces.FileService;
import com.trade_accounting.services.interfaces.api.FileApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileApi fileApi;

    private final String fileUrl;

    private final CallExecuteService<FileDto> dtoCallExecuteService;

    public FileServiceImpl(@Value("${file_url}") String fileUrl, CallExecuteService<FileDto> dtoCallExecuteService, Retrofit retrofit) {
        this.fileUrl = fileUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
        this.fileApi = retrofit.create(FileApi.class);
    }

    @Override
    public List<FileDto> getAll() {
        Call<List<FileDto>> fileDtoListCall = fileApi.getAll(fileUrl);
        return dtoCallExecuteService.callExecuteBodyList(fileDtoListCall, FileDto.class);
    }

    @Override
    public FileDto getById(Long id) {
        Call<FileDto> fileDtoCall = fileApi.getById(fileUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(fileDtoCall, FileDto.class, id);
    }

    @Override
    public List<FileDto> search(Map<String, String> query) {
        List<FileDto> fileDtoList = new ArrayList<>();
        Call<List<FileDto>> companyDtoListCall = fileApi.search(fileUrl, query);

        try {
            fileDtoList = companyDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка CompanyDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка CompanyDto - ", e);
        }

        return fileDtoList;
    }

    @Override
    public void create(FileDto fileDto) {
        Call<Void> fileDtoCall = fileApi.create(fileUrl, fileDto);
        dtoCallExecuteService.callExecuteBodyCreate(fileDtoCall, FileDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> fileDtoCall = fileApi.deleteById(fileUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(fileDtoCall, FileDto.class, id);
    }
}
