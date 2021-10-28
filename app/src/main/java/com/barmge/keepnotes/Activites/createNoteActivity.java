package com.barmge.keepnotes.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.barmge.keepnotes.R;
import com.barmge.keepnotes.database.NotesDatabase;
import com.barmge.keepnotes.entities.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class createNoteActivity extends AppCompatActivity {

    private EditText noteTitle , noteText;
    private TextView dateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        ImageView backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        noteText = findViewById(R.id.noteText);
        noteTitle = findViewById(R.id.noteTitle);
        dateTime = findViewById(R.id.dateAndTime);

        dateTime.setText(
                new SimpleDateFormat("dd mm yy HH:mm a" , Locale.getDefault())
                .format(new Date())
        );

        ImageView save = findViewById(R.id.saveNote);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });
    }

    private void saveNote(){
        if (noteTitle.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Title Require", Toast.LENGTH_SHORT).show();
            return;
        }else if (noteText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Text Require", Toast.LENGTH_SHORT).show();
            return;
        }

        final Notes notes = new Notes();
        notes.setTitle(noteTitle.getText().toString());
        notes.setNoteText(noteText.getText().toString());
        notes.setDateTime(dateTime.getText().toString());

        @SuppressLint("StaticFieldLeak")
        class saveNoteTask extends AsyncTask<Void , Void , Void>{

            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getDatabase(getApplicationContext()).notesDao().insertNotes(notes);
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                Intent intent = new Intent();
                setResult(RESULT_OK , intent);
                finish();
            }
        }
        new saveNoteTask().execute();
    }
}