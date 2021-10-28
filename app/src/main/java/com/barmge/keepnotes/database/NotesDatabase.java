package com.barmge.keepnotes.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.barmge.keepnotes.dao.NotesDao;
import com.barmge.keepnotes.entities.Notes;

@Database(entities = Notes.class , version = 1 , exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {

    public static NotesDatabase notesDatabase;

    public static synchronized NotesDatabase getDatabase(Context context) {
        if (notesDatabase == null) {
            notesDatabase = Room.databaseBuilder(
                    context,
                    NotesDatabase.class,
                    "notes_db"
            ).build();
        }
        return notesDatabase;
    }

    public abstract NotesDao notesDao();



}
