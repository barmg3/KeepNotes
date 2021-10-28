package com.barmge.keepnotes.Activites;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.barmge.keepnotes.R;
import com.barmge.keepnotes.adapters.NoteAdapter;
import com.barmge.keepnotes.database.NotesDatabase;
import com.barmge.keepnotes.entities.Notes;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView noteRecycleView;
    private List<Notes> notesList;
    private NoteAdapter noteAdapter;
    ImageView addNewNote;

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        getNotes();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        noteRecycleView = findViewById(R.id.noteRecycler);
        noteRecycleView.setLayoutManager(
                new StaggeredGridLayoutManager(2 , StaggeredGridLayoutManager.VERTICAL)
        );
        notesList = new ArrayList<>();
        noteAdapter = new NoteAdapter(notesList);
        noteRecycleView.setAdapter(noteAdapter);
        getNotes();

        addNewNote = findViewById(R.id.addButton);
        addNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartForResult.launch(new Intent(MainActivity.this, createNoteActivity.class));

            }
        });


    }

    private void getNotes(){
        @SuppressLint("StaticFieldLeak")
        class getNotesTask extends AsyncTask<Void , Void , List<Notes>>{

            @Override
            protected List<Notes> doInBackground(Void... voids) {
                return NotesDatabase.
                        getDatabase(getApplicationContext())
                        .notesDao().getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Notes> notes) {
                super.onPostExecute(notes);
                if (notesList.size() == 0){
                    notesList.addAll(notes);
                    noteAdapter.notifyDataSetChanged();


                }else{
                    notesList.add(0, notes.get(0));
                    noteAdapter.notifyItemInserted(0);
                }
                noteRecycleView.smoothScrollToPosition(0);
            }
        }
        new getNotesTask().execute();
    }
}