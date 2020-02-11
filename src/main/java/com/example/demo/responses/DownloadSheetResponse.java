package com.example.demo.responses;

import com.example.demo.DTO.StringsDTO;
import com.example.demo.services.OdsService;
import com.example.demo.services.XlsService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.springframework.http.converter.json.AbstractJsonHttpMessageConverter.*;

/**
 * @version 1.0
 * @title Exportador e conversor de planilhas
 * @comment Api para exportar dados de um determinado servidor convertidos para uma planilha de Excel, nos seguintes formatos: XLS, XLSX, ODS.
 * @code Tipos de Content
 * @code Type: XLS: application/vnd.ms-excel
 * @code XLSX: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
 * @code ODS: application/vnd.oasis.opendocument.spreadsheet
 * @throws IOException
 * @since 11-02-2020
 **/

@RestController
@RequestMapping(value = "/download")
public class DownloadSheetResponse {

    @PostMapping("/sheet/{content-type}")
    public ResponseEntity optionalDownload(@RequestBody StringsDTO stringsDTOS, @PathVariable("content-type") String contentType) throws IOException {
        System.out.println(stringsDTOS.getHeaders());
        switch (contentType) {
            case "xls":
                return ResponseEntity
                        .ok()
                        .contentType(new MediaType("application", "vnd.ms-excel", DEFAULT_CHARSET))
                        .header("content-disposition", "attachment; filename = ".concat(stringsDTOS.getFileName().concat(".xls")))
                        .body(new ByteArrayInputStream(XlsService
                                .exportExcel(stringsDTOS.getHeaders(),
                                        stringsDTOS.getContents(),
                                        stringsDTOS.getDirName(),
                                        stringsDTOS.getFileName(),
                                        stringsDTOS.getTitleTag())
                                .toByteArray()));
            case "ods":
                return ResponseEntity
                        .ok()
                        .contentType(new MediaType("application", "vnd.oasis.opendocument.spreadsheet", DEFAULT_CHARSET))
                        .header("content-disposition", "attachment; filename = ".concat(stringsDTOS.getFileName().concat(".xlsx")))
                        .body(new ByteArrayInputStream(OdsService
                                .exportExcel(stringsDTOS.getHeaders(),
                                        stringsDTOS.getContents(),
                                        stringsDTOS.getDirName(),
                                        stringsDTOS.getFileName(),
                                        stringsDTOS.getTitleTag(),
                                        stringsDTOS.getLocal().get(0),
                                        stringsDTOS.getLocal().get(1))
                                .toByteArray()));
            case "xlsx":
                return ResponseEntity
                        .ok()
                        .contentType(new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet", DEFAULT_CHARSET))
                        .header("content-disposition", "attachment; filename = ".concat(stringsDTOS.getFileName().concat(".ods")))
                        .body(new ByteArrayInputStream(XlsService
                                .exportExcel(stringsDTOS.getHeaders(),
                                        stringsDTOS.getContents(),
                                        stringsDTOS.getDirName(),
                                        stringsDTOS.getFileName(),
                                        stringsDTOS.getTitleTag())
                                .toByteArray()));
            default:
                return ResponseEntity.noContent().build();
        }
    }
}
