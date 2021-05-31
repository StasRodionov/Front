package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.ImageDto;
import com.trade_accounting.services.interfaces.ImageService;
import com.trade_accounting.services.interfaces.api.ImageApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageApi imageApi;
    private final String imageUrl;
    private ImageDto imageDto;

    public ImageServiceImpl(@Value("${image_url}") String imageUrl, Retrofit retrofit) {
        imageApi = retrofit.create(ImageApi.class);
        this.imageUrl = imageUrl;
    }

    @Override
    public List<ImageDto> getAll() {
        List<ImageDto> imageDtoList = new ArrayList<>();
        Call<List<ImageDto>> imageDtoListCall = imageApi.getAll(imageUrl);

        try {
            imageDtoList.addAll(Objects.requireNonNull(imageDtoListCall.execute().body()));
            log.info("Успешно выполнен запрос на получение списка ImageDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на получение списка ImageDto: {}", e);
        }

        return imageDtoList;
    }

    @Override
    public ImageDto getById(Long id) {
        Call<ImageDto> imageDtoCall = imageApi.getById(imageUrl, id);

        try {
            imageDto = imageDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение экземпляра ImageDto с id = {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на получение ImageDto с id = {}: {}",
                    id, e);
        }

        return imageDto;
    }

    @Override
    public ImageDto create(ImageDto imageDto) {
        Call<ImageDto> imageDtoCall = imageApi.create(imageUrl, imageDto);

        try {
            return imageDtoCall.execute().body();
        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на создание нового экземпляра ImageDto {}: {}",
                    imageDto, e);
        }
        return imageDto;
    }

    @Override
    public ImageDto update(ImageDto imageDto) {
        Call<ImageDto> imageDtoCall = imageApi.update(imageUrl, imageDto);

        try {
            ImageDto response = imageDtoCall.execute().body();
            log.info("Успешно выполнен запрос на обновление ImageDto {}", response);
            return response;

        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на обновление ImageDto {}: {}",
                    imageDto, e);
        }
        return imageDto;
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> imageDtoCall = imageApi.deleteById(imageUrl, id);
        Response<Void> response;

        try {
            response = imageDtoCall.execute();
            if (response.isSuccessful()) {
                log.info("Успешно выполнен запрос на удаление ImageDto c id = {}", id);
            } else {
                log.error("Произошла ошибка при выполнении запроса на удаление ImageDto с id= {} - {}",
                        id, response.errorBody());
            }

        } catch (IOException e) {
            log.error("Произошла ошибка при отправке запроса на удаление ImageDto c id = {}: {}", id, e);
        }
    }
}