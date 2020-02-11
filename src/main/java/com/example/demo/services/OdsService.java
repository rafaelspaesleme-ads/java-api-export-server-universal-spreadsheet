package com.example.demo.services;

import com.github.jferard.fastods.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import static com.example.demo.enums.Extension.ods;

@Service
public class OdsService {

    private static Random random = new Random();
    private static String underline = "_";
    private static String point = ".";

    public static ByteArrayOutputStream exportExcel(List<String> stringHeaderList, List<List<String>> stringContentList, String dirName, String fileName, String titleTag, String lang, String country) throws IOException {
        return builderSheet(stringHeaderList, stringContentList, dirName, fileName, titleTag, lang, country);
    }

    private static ByteArrayOutputStream builderSheet(List<String> stringHeaderList, List<List<String>> stringContentList, String dirName, String fileName, String titleTag, String lang, String country) throws IOException {
        AtomicInteger rownum = new AtomicInteger();

        final OdsFactory odsFactory = OdsFactory.create(Logger.getLogger(titleTag), new Locale(lang, country));
        final AnonymousOdsFileWriter writer = odsFactory.createWriter();
        final OdsDocument document = writer.document();

        final Table table = document.addTable(titleTag);

        AtomicInteger cellnum2 = new AtomicInteger();

        ObjectListService.builderHeaders(stringHeaderList).forEach(header -> {
            try {
                final TableRowImpl row = table.getRow(0);
                row.getOrCreateCell(cellnum2.getAndIncrement()).setStringValue(header);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        rownum.set(1);
        stringContentList.forEach(strings -> {
            try {
                TableRowImpl row = table.getRow(rownum.getAndIncrement());
                strings.forEach(content -> {
                    TableRowImpl[] rows = {row};
                    AtomicInteger cellNumber = new AtomicInteger();
                    IntStream.range(0, strings.size())
                            .forEach(index -> {
                                AtomicInteger idx = new AtomicInteger();
                                rows[idx.getAndIncrement()].getOrCreateCell(cellNumber.getAndIncrement()).setStringValue(strings.get(index));
                            });
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        File file = new File(dirName, fileName.concat(underline.concat(String.valueOf(random).concat(point)).concat(String.valueOf(ods))));
        FileOutputStream fos = new FileOutputStream(file);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            writer.saveAs(file);
            baos.writeTo(fos);
            return baos;
        }
    }
}
