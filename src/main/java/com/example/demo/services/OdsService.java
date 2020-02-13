package com.example.demo.services;

import com.github.jferard.fastods.*;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import static com.example.demo.enums.Extension.ods;

@Service
public class OdsService {

    private Random random = new Random();
    private String underline = "_";
    private String point = ".";

    public ByteArrayInputStream exportExcel(List<String> stringHeaderList, List<List<String>> stringContentList, String dirName, String fileName, String titleTag, String lang, String country) throws IOException {
        return builderSheet(stringHeaderList, stringContentList, dirName, fileName, titleTag, lang, country);
    }

    private ByteArrayInputStream builderSheet(List<String> stringHeaderList, List<List<String>> stringContentList, String dirName, String fileName, String titleTag, String lang, String country) throws IOException {
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
            writer.saveAs(file);
            return new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
    }
}
