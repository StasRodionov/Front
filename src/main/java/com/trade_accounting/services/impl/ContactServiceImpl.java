package com.trade_accounting.services.impl;

import com.trade_accounting.controllers.dto.ContactDto;
import com.trade_accounting.services.interfaces.ContactService;
import com.trade_accounting.services.interfaces.api.ContactApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class ContactServiceImpl implements ContactService {

    private final ContactApi contactApi;
    private final String contactUrl;
    private ContactDto contactDto;
    private final CallExecuteService<ContactDto> dtoCallExecuteService;


    public ContactServiceImpl(@Value("${contact_url}") String contactUrl, Retrofit retrofit, CallExecuteService<ContactDto> dtoCallExecuteService) {
        contactApi = retrofit.create(ContactApi.class);
        this.contactUrl = contactUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<ContactDto> getAll() {
        Call<List<ContactDto>> contactDtoListCall = contactApi.getAll(contactUrl);
        return dtoCallExecuteService.callExecuteBodyList(contactDtoListCall, ContactDto.class);
    }

    @Override
    public ContactDto getById(Long id) {
        Call<ContactDto> contactDtoCall = contactApi.getById(contactUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(contactDtoCall, ContactDto.class, id);
    }


    @Override
    public ContactDto create(ContactDto contactDto) {
        Call<ContactDto> contactDtoCall = contactApi.create(contactUrl, contactDto);

        try {
            this.contactDto=contactDtoCall.execute().body();
            log.info("Успешно выполнен запрос на создание экземпляра ContactDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на создание экземпляра ContactDto - {}", e);
        }
        return this.contactDto;
    }

    @Override
    public void update(ContactDto contactDto) {
        Call<Void> contactDtoCall = contactApi.update(contactUrl, contactDto);
        dtoCallExecuteService.callExecuteBodyUpdate(contactDtoCall, ContactDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> contactDtoCall = contactApi.deleteById(contactUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(contactDtoCall, ContactDto.class, id);
    }


}
