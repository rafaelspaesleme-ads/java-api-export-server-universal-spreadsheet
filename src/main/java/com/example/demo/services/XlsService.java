package com.example.demo.services;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static com.example.demo.enums.Extension.*;

@Service
public class XlsService {

    private static Random random = new Random();
    private static String underline = "_";
    private static String point = ".";

    public static ByteArrayOutputStream exportExcel(List<String> stringHeaderList, List<List<String>> stringContentList, String dirName, String fileName, String titleTag) throws IOException {
        return builderSheet(stringHeaderList, stringContentList, dirName, fileName, titleTag);
    }

    private static ByteArrayOutputStream builderSheet(List<String> stringHeaderList, List<List<String>> stringContentList, String dirName, String fileName, String titleTag) throws IOException {
        AtomicInteger rownum = new AtomicInteger();

        final Workbook workbook = new HSSFWorkbook();
        final Sheet sheet = workbook.createSheet(titleTag);

        AtomicInteger cellnum = new AtomicInteger();
        final Row rw = sheet.createRow(0);

        stringHeaderList.forEach(header -> {
            rw.createCell(cellnum.getAndIncrement()).setCellValue(header);
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
        FileOutputStream fos = new FileOutputStream(new File(dirName, fileName.concat(underline.concat(String.valueOf(random).concat(point)).concat(String.valueOf(xlsx)))));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            workbook.write(fos);
            baos.writeTo(fos);
            return baos;
        } finally {
            workbook.close();
            baos.close();
        }
    }
}