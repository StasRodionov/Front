package com.trade_accounting.services.interfaces.util;

import com.trade_accounting.models.dto.util.ImageDto;

import java.util.List;

public interface ImageService {

    List<ImageDto> getAll();

    ImageDto getById(Long id);

    void create(ImageDto imageDto);

    void update(ImageDto imageDto);

    void deleteById(Long id);
}