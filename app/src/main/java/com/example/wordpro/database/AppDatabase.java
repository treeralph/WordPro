package com.example.wordpro.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Sentence.class, TodaySentence.class, Period.class, Content.class, TeamStudy.class, TeamMate.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SentenceDao sentenceDao();
    public abstract TodaySentenceDao todaySentenceDao();
    public abstract PeriodDao periodDao();
    public abstract ContentDao contentDao();
    public abstract TeamStudyDao teamStudyDao();
    public abstract TeamMateDao teamMateDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDBInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "AppDB")
                    .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }
}
