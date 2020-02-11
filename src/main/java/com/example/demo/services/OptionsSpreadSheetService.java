package com.example.demo.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class OptionsSpreadSheetService {
    public static void exportSpreadSheet(List<String> stringHeaderList, List<List<String>> stringContentList, String dirName, String fileName, String titleTag, String typeContent, String lang, String country) throws IOException {
        switch (typeContent) {
            case "application/vnd.ms-excel":
                XlsService.exportExcel(stringHeaderList, stringContentList, dirName, fileName, titleTag);
                break;
            case "application/vnd.oasis.opendocument.spreadsheet":
                OdsService.exportExcel(stringHeaderList, stringContentList, dirName, fileName, titleTag, lang, country);
                break;
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                XlsxService.exportExcel(stringHeaderList, stringContentList, dirName, fileName, titleTag);
                break;
        }
    }
}
