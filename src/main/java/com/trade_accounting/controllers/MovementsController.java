package com.trade_accounting.controllers;

import lombok.RequiredArgsConstructor;

import org.apache.xmlbeans.SystemProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
}
