package com.trade_accounting.services.interfaces.util;

import com.trade_accounting.models.dto.util.FileDto;

import java.util.List;
import java.util.Map;

public interface FileService {

    List<FileDto> getAll();

    FileDto getById(Long id);

    List<FileDto> search(Map<String, String> query);

    void create(FileDto fileDto);

    void deleteById(Long id);
}
