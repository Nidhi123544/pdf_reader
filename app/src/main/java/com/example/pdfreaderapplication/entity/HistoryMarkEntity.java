package com.example.pdfreaderapplication.entity;

public class HistoryMarkEntity {
    int historyMarkId;
    String historyMarkFileName;


    public HistoryMarkEntity(int historyMarkId, String historyMarkFileName) {
        this.historyMarkId = historyMarkId;
        this.historyMarkFileName = historyMarkFileName;
    }

    public int getHistoryMarkId() {
        return historyMarkId;
    }

    public void setHistoryMarkId(int historyMarkId) {
        this.historyMarkId = historyMarkId;
    }

    public String getHistoryMarkFileName() {
        return historyMarkFileName;
    }

    public void setHistoryMarkFileName(String historyMarkFileName) {
        this.historyMarkFileName = historyMarkFileName;
    }

}