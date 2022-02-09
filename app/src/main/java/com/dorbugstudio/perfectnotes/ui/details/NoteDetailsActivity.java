package com.dorbugstudio.perfectnotes.ui.details;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.dorbugstudio.perfectnotes.R;
import com.dorbugstudio.perfectnotes.domain.Note;


public class NoteDetailsActivity extends AppCompatActivity {

    private static final String EXTRA_NOTE = "EXTRA_NOTE";

    public static void show(Context context, Note note) {
        Intent intent = new Intent(context, NoteDetailsActivity.class);
        intent.putExtra(EXTRA_NOTE, note);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        if (savedInstanceState == null) {

            Note note = (Note) getIntent().getSerializableExtra(EXTRA_NOTE);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_list,
                            NoteDetailsFragment.newInstance(note))
                    .commit();

        }
    }
}