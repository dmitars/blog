package com.example.springExample.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public interface DatedData {
    void setDate(String date);

    default void updateDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd.MM.yyyy");
        setDate(sdf.format(new Date()));
    }
}
