package com.dorbugstudio.perfectnotes.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.dorbugstudio.perfectnotes.R;
import com.dorbugstudio.perfectnotes.ui.list.NotesListFragment;
import com.dorbugstudio.perfectnotes.ui.settings.SettingsFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationDrawable {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer);

        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.action_goto_main) {
                    showMainFragment();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }

                if (item.getItemId() == R.id.action_about) {
                    Toast.makeText(MainActivity.this, "About clicked", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }

                if (item.getItemId() == R.id.action_settings) {
                    showSettings();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }

                return false;
            }
        });

        showMainFragment();
    }

    private void showMainFragment() {
        Fragment noteListFragment = getSupportFragmentManager().findFragmentByTag(NotesListFragment.TAG);

        if (noteListFragment == null) {
            noteListFragment = new NotesListFragment();
        } else {
            if (noteListFragment.isVisible()) {
                return;
            }
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_container,
                        noteListFragment,
                        NotesListFragment.TAG)
                .commitAllowingStateLoss();
    }

    private void showSettings() {
        Fragment settingsFragment = getSupportFragmentManager().findFragmentByTag(SettingsFragment.TAG);

        if (settingsFragment == null) {
            settingsFragment = new SettingsFragment();
        } else {
            if (settingsFragment.isVisible()) {
                return;
            }
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_container,
                        settingsFragment,
                        SettingsFragment.TAG)
                .addToBackStack("SettingsFragment")
                .commitAllowingStateLoss();
    }

    @Override
    public void setAppBar(MaterialToolbar toolbar) {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open_drawer,
                R.string.close_drawer
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }
}