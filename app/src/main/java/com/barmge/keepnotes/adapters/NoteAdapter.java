package com.barmge.keepnotes.adapters;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barmge.keepnotes.R;
import com.barmge.keepnotes.entities.Notes;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{

    private List<Notes> notes;

    public NoteAdapter(List<Notes> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.note_container,
                        parent,false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.setNote(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView notesTitle , notesDateTime , notesText;
        LinearLayout notesLayout;
        RoundedImageView imageNoteContanier;

       NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            notesTitle = itemView.findViewById(R.id.notesTitle);
            notesDateTime = itemView.findViewById(R.id.dateTime);
            notesText = itemView.findViewById(R.id.notesText);
            notesLayout = itemView.findViewById(R.id.noteLayout);
            imageNoteContanier = itemView.findViewById(R.id.imageNoteContainer);

        }
        void setNote (Notes note){
           notesTitle.setText(note.getTitle());
           notesDateTime.setText(note.getDateTime());
           notesText.setText(note.getNoteText());

            GradientDrawable gradientDrawable = (GradientDrawable) notesLayout.getBackground();
            if(note.getColor() != null){
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            }else gradientDrawable.setColor(Color.parseColor("#80ffffff"));

            if (note.getImagePath() != null){
                imageNoteContanier.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
                imageNoteContanier.setVisibility(View.VISIBLE);
            }else imageNoteContanier.setVisibility(View.GONE);
        }
    }
}
