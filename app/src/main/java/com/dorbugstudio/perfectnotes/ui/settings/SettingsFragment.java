package com.dorbugstudio.perfectnotes.ui.settings;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dorbugstudio.perfectnotes.R;
import com.dorbugstudio.perfectnotes.ui.NavigationDrawable;
import com.dorbugstudio.perfectnotes.ui.details.NoteDetailsFragment;
import com.google.android.material.appbar.MaterialToolbar;

public class SettingsFragment extends Fragment {

    public static final String TAG = "SettingsFragment";

    public SettingsFragment() {
        super(R.layout.fragment_settings);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (requireActivity() instanceof NavigationDrawable) {
            MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
            ((NavigationDrawable) requireActivity()).setAppBar(toolbar);
        }

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

