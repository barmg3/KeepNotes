package com.barmge.keepnotes.Activites;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.barmge.keepnotes.R;
import com.barmge.keepnotes.database.NotesDatabase;
import com.barmge.keepnotes.entities.Notes;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class createNoteActivity extends AppCompatActivity {

    private EditText noteTitle , noteText;
    private TextView dateTime;
    private ImageView  imageColor1 , imageColor2 , imageColor3 , imageColor4 , imageColor5 , imageNote;
    private LinearLayout colorPicker , miscellanceous;

    private String selectedTextColor;
    private String selectedImagePath;

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 1;




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

        selectedImagePath = "";

        miscellanceous = findViewById(R.id.misce_layout);
        colorPicker = findViewById(R.id.miscr_color_layout);
        //Add Image Click Listener
        miscellanceous.findViewById(R.id.add_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission
                        (getApplicationContext() , Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(
                            createNoteActivity.this ,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION
                    );
                }else selectImage();

            }
        });

        //OnClick Listener to Show ColorPicker
        miscellanceous.findViewById(R.id.change_color).setOnClickListener(new View.OnClickListener() {
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
        //Assign Variable
        imageColor1 = colorPicker.findViewById(R.id.imageColor1);
        imageColor2 = colorPicker.findViewById(R.id.imageColor2);
        imageColor3 = colorPicker.findViewById(R.id.imageColor3);
        imageColor4 = colorPicker.findViewById(R.id.imageColor4);
        imageColor5 = colorPicker.findViewById(R.id.imageColor5);

        //OnClick Listener for change the color and put image on selected color
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
        });
        colorPicker.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
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


        imageNote = findViewById(R.id.imageNote);

        //Assign Variable
        noteText = findViewById(R.id.noteText);
        noteTitle = findViewById(R.id.noteTitle);
        dateTime = findViewById(R.id.dateAndTime);

        dateTime.setText(
                new SimpleDateFormat("DD MM yy HH:mm a", Locale.getDefault())
                .format(new Date())
        );

        //Save Note Click Listener
        ImageView save = findViewById(R.id.saveNote);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });
    }
    //Save Note Function
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
        notes.setColor(selectedTextColor);
        notes.setImagePath(selectedImagePath);

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
    //Text Color Changer Function
    private void setNoteTextColor(){
        noteText.getTextColors().getDefaultColor();
        noteText.setTextColor(Color.parseColor(selectedTextColor));
    }

    ActivityResultLauncher<Intent> imageStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            Uri selectedImagUri = intent.getData();
                            if(selectedImagUri != null){
                                try{
                                    InputStream inputStream = getContentResolver().openInputStream(selectedImagUri);
                                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                    imageNote.setImageBitmap(bitmap);
                                    imageNote.setVisibility(VISIBLE);
                                    selectedImagePath = getPathFromUri(selectedImagUri);

                                }catch (Exception exception){

                                }
                            }
                        }

                    }
                }
            });

    private void selectImage(){
        imageStartForResult.launch(new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                selectImage();
            }else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getPathFromUri(Uri contentUri){
        String filePath;
        Cursor cursor = getContentResolver()
                .query(contentUri , null , null , null);
        if(cursor == null){
            filePath = contentUri.getPath();
        }else{
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }
}