package com.example.demo.DTO;

import java.util.List;

public class StringsDTO {

    private List<String> headers;
    private List<List<String>> contents;
    private String dirName;
    private String fileName;
    private String titleTag;
    private List<String> local;

    public StringsDTO() {
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<List<String>> getContents() {
        return contents;
    }

    public void setContents(List<List<String>> contents) {
        this.contents = contents;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTitleTag() {
        return titleTag;
    }

    public void setTitleTag(String titleTag) {
        this.titleTag = titleTag;
    }

    public List<String> getLocal() {
        return local;
    }

    public void setLocal(List<String> local) {
        this.local = local;
    }
}
