package com.dorbugstudio.perfectnotes.ui.settings;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dorbugstudio.perfectnotes.R;
import com.dorbugstudio.perfectnotes.ui.details.NoteDetailsFragment;

public class SettingsFragment extends Fragment {

    public static final String TAG = "SettingsFragment";

    public SettingsFragment() {
        super(R.layout.settings_details);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.settings_text).setOnClickListener(v -> showNoteDetailsExample());
    }

    private void showNoteDetailsExample() {
        if (getChildFragmentManager().findFragmentByTag(NoteDetailsFragment.TAG) == null) {

            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings_child_fragment_container,
                            NoteDetailsFragment.newInstance(0),
                            NoteDetailsFragment.TAG)
                    .commitAllowingStateLoss();

        }
    }
}

