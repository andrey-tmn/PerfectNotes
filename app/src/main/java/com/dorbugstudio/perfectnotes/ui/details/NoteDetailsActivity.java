package com.dorbugstudio.perfectnotes.ui.details;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.dorbugstudio.perfectnotes.R;


public class NoteDetailsActivity extends AppCompatActivity {

    private static final String EXTRA_NOTE_ID = "EXTRA_NOTE_ID";

    public static void show(Context context, int noteId) {
        Intent intent = new Intent(context, NoteDetailsActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, noteId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        if (savedInstanceState == null) {

            int noteId = getIntent().getIntExtra(EXTRA_NOTE_ID, 0);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_list,
                            NoteDetailsFragment.newInstance(noteId))
                    .commit();

        }
    }
}