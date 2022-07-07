package com.trade_accounting.components.apps.impl.retail;

import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.models.dto.retail.EventLogDto;
import com.trade_accounting.services.api.retail.RetailEventLogApi;
import com.trade_accounting.services.interfaces.retail.RetailEventLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RetailEventLogServiceImpl implements RetailEventLogService {

    private final RetailEventLogApi retailEventLogApi;

    private final String retailEventLogUrl;

    private final CallExecuteService<EventLogDto> dtoCallExecuteService;

    public RetailEventLogServiceImpl(@Value("${retail_event_log_url}") String retailEventLogUrl,
                                       CallExecuteService<EventLogDto> dtoCallExecuteService,
                                       Retrofit retrofit) {
        this.retailEventLogApi = retrofit.create(RetailEventLogApi.class);
        this.retailEventLogUrl = retailEventLogUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<EventLogDto> getAll() {
        Call<List<EventLogDto>> retailEventLogApiList = retailEventLogApi.getAll(retailEventLogUrl);
        return dtoCallExecuteService.callExecuteBodyList(retailEventLogApiList, EventLogDto.class);
    }

    @Override
    public List<EventLogDto> search(Map<String, String> query) {
        return null;
    }

    @Override
    public EventLogDto getById(Long id) {
        return null;
    }

    @Override
    public void create(EventLogDto eventLogDto) {

    }

    @Override
    public void update(EventLogDto eventLogDto) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<EventLogDto> findBySearch(String search) {
        return null;
    }
}
