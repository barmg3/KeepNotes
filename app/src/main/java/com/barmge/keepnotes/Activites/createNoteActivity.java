package com.barmge.keepnotes.Activites;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private ImageView changeColor , imageColor1 , imageColor2 , imageColor3 , imageColor4 , imageColor5;
    private LinearLayout colorPicker;

    private String selectedTextColor;

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

        colorPicker = findViewById(R.id.miscr_color_layout);
        changeColor = findViewById(R.id.change_color);
        changeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(colorPicker.getVisibility() != VISIBLE){
                colorPicker.setVisibility(VISIBLE);
                }else {
                    colorPicker.setVisibility(INVISIBLE);
                }

            }
        });

//        selectedTextColor = "#00000";
//        setNoteTextColor();
        imageColor1 = colorPicker.findViewById(R.id.imageColor1);
        imageColor2 = colorPicker.findViewById(R.id.imageColor2);
        imageColor3 = colorPicker.findViewById(R.id.imageColor3);
        imageColor4 = colorPicker.findViewById(R.id.imageColor4);
        imageColor5 = colorPicker.findViewById(R.id.imageColor5);

        colorPicker.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTextColor = "#C0C0C0";
                imageColor1.setImageResource(R.drawable.ic_check);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setNoteTextColor();
            }
        });
        colorPicker.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTextColor = "#FF80ED";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(R.drawable.ic_check);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setNoteTextColor();
            }
        });
        colorPicker.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTextColor = "#C9A0DC";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(R.drawable.ic_check);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setNoteTextColor();
            }
        });colorPicker.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTextColor = "#ff7373";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(R.drawable.ic_check);
                imageColor5.setImageResource(0);
                setNoteTextColor();
            }
        });
        colorPicker.findViewById(R.id.viewColor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTextColor = "#407294";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(R.drawable.ic_check);
                setNoteTextColor();
            }
        });


        noteText = findViewById(R.id.noteText);
        noteTitle = findViewById(R.id.noteTitle);
        dateTime = findViewById(R.id.dateAndTime);

        dateTime.setText(
                new SimpleDateFormat("DD MM yy HH:mm a", Locale.getDefault())
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

    private void setNoteTextColor(){
        noteText.getTextColors().getDefaultColor();
        noteText.setTextColor(Color.parseColor(selectedTextColor));
    }

}