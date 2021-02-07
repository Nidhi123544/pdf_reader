package com.example.pdfreaderapplication.entity;

import java.util.ArrayList;

public class HistoryMarkEntity {
    int historyMarkId;
    String historyMarkFileName;
    ArrayList<String> listData;

    public HistoryMarkEntity(int historyMarkId, String historyMarkFileName) {
        this.historyMarkId = historyMarkId;
        this.historyMarkFileName = historyMarkFileName;
    }
    public HistoryMarkEntity(){

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

    public ArrayList<String> getListData() {
        return listData;
    }

    public void setListData(ArrayList<String> listData) {
        this.listData = listData;
    }
}