package com.dorbugstudio.perfectnotes.ui.list;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dorbugstudio.perfectnotes.R;
import com.dorbugstudio.perfectnotes.domain.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NoteViewHolder> {

    private final List<Note> notes = new ArrayList<>();
    private OnNoteClicked onNoteClicked;
    private OnNoteLongClicked onNoteLongClicked;
    private int longClickNoteId = -1;

    public int getLongClickNoteId() {
        return longClickNoteId;
    }

    public void setLongClickNoteId(int longClickNoteId) {
        this.longClickNoteId = longClickNoteId;
    }

    public OnNoteClicked getOnNoteClicked() {
        return onNoteClicked;
    }

    public void setOnNoteClicked(OnNoteClicked onNoteClicked) {
        this.onNoteClicked = onNoteClicked;
    }

    public void setOnNoteLongClicked(OnNoteLongClicked onNoteLongClicked) {
        this.onNoteLongClicked = onNoteLongClicked;
    }

    public OnNoteLongClicked getOnNoteLongClicked() {
        return onNoteLongClicked;
    }

    public void setNotes(List<Note> notes) {
        this.notes.clear();
        this.notes.addAll(notes);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.title.setText(note.getTitle());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    interface OnNoteClicked {
        void onNoteClicked(int noteId);
    }

    interface OnNoteLongClicked {
        void OnNoteLongClicked(int noteId);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

        TextView title;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            View item = itemView.findViewById(R.id.list_item_card);
            item.setOnCreateContextMenuListener(this);
            item.setOnClickListener(this);

            title = item.findViewById(R.id.note_title_in_list);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            int clickedAt = getAdapterPosition();
            setLongClickNoteId(notes.get(clickedAt).getId());
        }

        @Override
        public void onClick(View view) {
            if (getOnNoteClicked() != null) {
                int clickedAt = getAdapterPosition();
                getOnNoteClicked().onNoteClicked(notes.get(clickedAt).getId());
            }
        }
    }
}