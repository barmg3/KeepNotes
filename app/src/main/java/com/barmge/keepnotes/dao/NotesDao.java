package com.barmge.keepnotes.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.barmge.keepnotes.entities.Notes;

import java.util.List;

@Dao
public interface NotesDao {

    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<Notes> getAllNotes();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertNotes(Notes notes);

    @Delete
    void deletNotes(Notes notes);
}
