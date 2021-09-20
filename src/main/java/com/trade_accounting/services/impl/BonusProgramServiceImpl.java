package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.BonusProgramDto;
import com.trade_accounting.services.interfaces.BonusProgramService;
import com.trade_accounting.services.interfaces.api.BonusProgramApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class BonusProgramServiceImpl implements BonusProgramService {
    private final BonusProgramApi bonusProgramApi;

    private final String bonusProgramUrl;

    private BonusProgramDto bonusProgramDto;

    private final CallExecuteService<BonusProgramDto> dtoCallExecuteService;

    public BonusProgramServiceImpl(Retrofit retrofit, @Value("${bonus_program_url}") String bonusProgramUrl,
                                   CallExecuteService<BonusProgramDto> dtoCallExecuteService) {
        this.bonusProgramApi = retrofit.create(BonusProgramApi.class);
        this.bonusProgramUrl = bonusProgramUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<BonusProgramDto> getAll() {
        List<BonusProgramDto> bonusProgramDtoList = new ArrayList<>();
        Call<List<BonusProgramDto>> bonusProgramDtoListCall = bonusProgramApi.getAll(bonusProgramUrl);
        return dtoCallExecuteService.callExecuteBodyList(bonusProgramDtoListCall, BonusProgramDto.class);
    }

    @Override
    public BonusProgramDto getById(Long id) {
        Call<BonusProgramDto> bonusProgramDtoCall = bonusProgramApi.getById(bonusProgramUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(bonusProgramDtoCall, bonusProgramDto, BonusProgramDto.class, id);
    }

    @Override
    public void create(BonusProgramDto bonusProgramDto) {
        Call<Void> bonusProgramDtoCall = bonusProgramApi.create(bonusProgramUrl, bonusProgramDto);
        dtoCallExecuteService.callExecuteBodyCreate(bonusProgramDtoCall, BonusProgramDto.class);
    }

    @Override
    public void update(BonusProgramDto bonusProgramDto) {
        Call<Void> bonusProgramDtoCall = bonusProgramApi.update(bonusProgramUrl, bonusProgramDto);
        dtoCallExecuteService.callExecuteBodyUpdate(bonusProgramDtoCall, BonusProgramDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> bonusProgramDtoCall = bonusProgramApi.deleteById(bonusProgramUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(bonusProgramDtoCall, BonusProgramDto.class, id);
    }
}
