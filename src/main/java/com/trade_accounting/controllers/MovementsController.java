package com.trade_accounting.controllers;

import com.trade_accounting.services.interfaces.util.EmailService;
import lombok.RequiredArgsConstructor;

import org.apache.xmlbeans.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movements/files")
public class MovementsController {

    @Autowired
    EmailService emailService;

    //TODO Add xls file template updating service
    //TODO Add xls to pdf converter service

    @GetMapping(value = "/{file-name}/{format}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody
    byte[] getFileByName(HttpServletResponse httpServletResponse,
                         @PathVariable(name = "file-name") String fileName,
                         @PathVariable(name = "format") String format) {

        httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + "." + format);

        Optional<byte[]> bytes = Optional.empty();

        try (FileInputStream fileInputStream = new FileInputStream(
                Path.of(SystemProperties.getProperty("user.dir")
                        + "/src/main/resources/files/" + fileName + "." + format).toFile())) {
            bytes = Optional.of(fileInputStream.readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes.orElse(new byte[0]);
    }

    @GetMapping(value = "/{send}/{file-name}/{file-extension}")
    public ResponseEntity<String> sendFileToEmail(
            @PathVariable(name = "file-name") String fileName,
            @PathVariable(name = "file-extension") String fileExtension) {

        emailService.sendMessageWithAttachment(
                //TODO Add email of current company or authenticated user
                "trade_test42@mail.ru",
                "Invoice : torg-13",
                "Torg 13 report",
                SystemProperties.getProperty("user.dir")
                        + "\\src\\main\\resources\\files\\" + fileName
                        + "." + fileExtension);

        return ResponseEntity.ok().build();
    }
}
