package com.example.wordpro.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "period")
public class Period {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "value")
    public int value;

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(id) + ": " + String.valueOf(value);
    }
}
