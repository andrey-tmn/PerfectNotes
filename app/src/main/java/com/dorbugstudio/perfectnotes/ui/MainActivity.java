package com.dorbugstudio.perfectnotes.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import android.os.Bundle;
import android.util.Log;

import com.dorbugstudio.perfectnotes.R;
import com.dorbugstudio.perfectnotes.ui.details.NoteDetailsFragment;
import com.dorbugstudio.perfectnotes.ui.list.NotesListFragment;

public class MainActivity extends AppCompatActivity implements FragmentResultListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .setFragmentResultListener(NotesListFragment.NOTE_SELECTED,
                        this, this);

        Log.d("TestNPTags", "onCreate");
    }

    @Override
    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_container, NoteDetailsFragment.newInstance(1))
                .addToBackStack("NoteDetailsFragment")
                .commit();
    }
}