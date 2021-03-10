package com.trade_accounting.services.interfaces;

import com.trade_accounting.models.dto.ImageDto;
import com.vaadin.flow.server.StreamResource;

import java.util.List;

public interface ImageService {

    List<ImageDto> getAll();

    ImageDto getById(Long id);

    ImageDto create(ImageDto imageDto);

    void update(ImageDto imageDto);

    void deleteById(Long id);

    StreamResource loadImage(ImageDto imageDto);
}