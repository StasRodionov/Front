package com.trade_accounting.services.interfaces.client;

import com.trade_accounting.models.dto.client.RoleDto;

import java.util.List;

public interface RoleService {

    List<RoleDto> getAll();

    RoleDto getById(Long id);

    void create(RoleDto roleDto);

    void update(RoleDto roleDto);

    void deleteById(Long id);
}
