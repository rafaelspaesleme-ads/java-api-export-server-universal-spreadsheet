package com.example.demo.services;

import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static com.example.demo.enums.Extension.xlsx;

@Service
public class XlsxService {

    private static Random random = new Random();
    private static String underline = "_";
    private static String point = ".";

    public static ByteArrayOutputStream exportExcel(List<String> stringHeaderList, List<List<String>> stringContentList, String dirName, String fileName, String titleTag) throws IOException {
        return builderSheet(stringHeaderList, stringContentList, dirName, fileName, titleTag);
    }

    private static ByteArrayOutputStream builderSheet(List<String> stringHeaderList, List<List<String>> stringContentList, String dirName, String fileName, String titleTag) throws IOException {
        AtomicInteger rownum = new AtomicInteger();

        XSSFWorkbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet(titleTag);

        Row headerRow = sheet.createRow(0);

        AtomicInteger cellnum = new AtomicInteger();

        stringHeaderList.forEach(header -> {
            headerRow.createCell(cellnum.getAndIncrement()).setCellValue(header);
        });

        rownum.set(1);
        stringContentList.forEach(strings -> {
            Row row = sheet.createRow(rownum.getAndIncrement());
            strings.forEach(content -> {
                Row[] rows = {row};
                AtomicInteger cellNumber = new AtomicInteger();
                IntStream.range(0, strings.size())
                        .forEach(index -> {
                            AtomicInteger idx = new AtomicInteger();
                            rows[idx.getAndIncrement()].createCell(cellNumber.getAndIncrement()).setCellValue(strings.get(index));
                        });
            });
        });

        FileOutputStream fileOut = new FileOutputStream(new File(dirName, fileName.concat(underline.concat(String.valueOf(random).concat(point)).concat(String.valueOf(xlsx)))));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            workbook.write(fileOut);
            baos.writeTo(fileOut);
            return baos;
        } finally {
            workbook.close();
            baos.close();
        }
    }
}
