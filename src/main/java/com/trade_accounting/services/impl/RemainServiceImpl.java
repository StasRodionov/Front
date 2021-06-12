package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.RemainDto;
import com.trade_accounting.services.interfaces.RemainService;
import com.trade_accounting.services.interfaces.api.RemainApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;

import java.util.List;

@Service
@Slf4j
public class RemainServiceImpl implements RemainService {

    private final RemainApi remainApi;
    private final String remainUrl;

    public RemainServiceImpl(@Value("@{remain_url}") String remainUrl, Retrofit retrofit){
        remainApi = retrofit.create(RemainApi.class);
        this.remainUrl = remainUrl;
    }

    @Override
    public List<RemainDto> getAll() {
        return null;
    }

    @Override
    public RemainDto getById(Long id) {
        return null;
    }

    @Override
    public void create(RemainDto remainDto) {

    }

    @Override
    public void update(RemainDto remainDto) {

    }

    @Override
    public void deleteById(Long id) {

    }
}
