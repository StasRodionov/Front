package com.trade_accounting.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {

    private Long id;

    private byte[] content;

    private String sortNumber;

    private String content;

    private String fileName;

    public void setContentEncoder(byte[] bytes) {
        content = Base64.getEncoder().encodeToString(bytes);
    }
}