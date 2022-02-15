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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}