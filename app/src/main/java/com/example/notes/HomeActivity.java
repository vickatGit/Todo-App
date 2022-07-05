package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notes.Database.UserEntity;
import com.example.notes.Fragments.NewNoteFragment;
import com.example.notes.Fragments.NoteItemFragment;
import com.example.notes.Fragments.NotesFragment;
import com.example.notes.Fragments.TrashNoteItemFragment;
import com.example.notes.Fragments.TrashedNotesFragment;
import com.example.notes.Model.NoteModel;
import com.example.notes.UiModify.ToolbarHamburgerIcon;
import com.example.notes.ViewModels.HomeActivityandFragmentsViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements NotesFragment.newNoteMessage, NoteItemFragment.UpdateNote{

    private static final String TAG = "tag";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    HomeActivityandFragmentsViewModel viewModel;
    private String userId;
    Button signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        userId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        View headerLayout=navigationView.inflateHeaderView(R.layout.navigation_header);
        TextView navUsername=headerLayout.findViewById(R.id.user_name);
        navUsername.setText(userName);


        HomeActivityandFragmentsViewModel.setUserId(userId);
        viewModel = new ViewModelProvider(this).get(HomeActivityandFragmentsViewModel.class);
        viewModel.setParentActivity(this);
        signOut=findViewById(R.id.sign_out);


        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.add_icon);

        ActionBarDrawerToggle menuToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(menuToggle);
        menuToggle.syncState();
        menuToggle.setDrawerArrowDrawable(new ToolbarHamburgerIcon(this));
        getSupportFragmentManager().beginTransaction().replace(R.id.notes_fragment_container,new NotesFragment()).commit();
        navigationView.getMenu().getItem(0).setChecked(true);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.signOut();
                Toast.makeText(HomeActivity.this, "Log Out Successful", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d(TAG, "onNavigationItemSelected: "+item.getItemId());
                switch (item.getItemId()){
                    case R.id.trash_notes_menu:
                        getSupportFragmentManager().beginTransaction().replace(R.id.notes_fragment_container,new TrashedNotesFragment()).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.all_notes_menu:
                        getSupportFragmentManager().beginTransaction().replace(R.id.notes_fragment_container,new NotesFragment()).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                }
                return true;
            }
        });

    }
    public String getUserId(){
        return userId;
    }

    @Override
    public void onBackPressed() {

        if(findViewById(R.id.trash_notes_fragment)!=null){
            if(findViewById(R.id.trash_notes_fragment).isShown()){
                getSupportFragmentManager().beginTransaction().replace(R.id.notes_fragment_container, new NotesFragment()).commit();
                navigationView.getMenu().getItem(0).setChecked(true);

                Log.d(TAG, "onBackPressed: visible" + R.id.trash_notes_fragment);
            }

        }
        else if(findViewById(R.id.new_note_fragment)!=null) {
            if (findViewById(R.id.new_note_fragment).getId() == R.id.new_note_fragment) {
                getSupportFragmentManager().beginTransaction().replace(R.id.notes_fragment_container, new NotesFragment()).commit();
                navigationView.getMenu().getItem(0).setChecked(true);
                Log.d(TAG, "onBackPressed: visible" + R.id.new_note_fragment);
            }
        }
        else if(findViewById(R.id.trash_note_item_fragment)!=null){
            if(findViewById(R.id.trash_note_item_fragment).isShown()){
                navigationView.getMenu().getItem(1).setChecked(true);
                getSupportFragmentManager().popBackStack();
            }
        }
        else if(findViewById(R.id.note_item_fragment)!=null){
            if(findViewById(R.id.note_item_fragment).isShown()){
                navigationView.getMenu().getItem(0).setChecked(true);
                getSupportFragmentManager().popBackStack();
            }
        }
        else if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else if(findViewById(R.id.notes_fragment)!=null){
            if(findViewById(R.id.notes_fragment).isShown()){
                finish();
            }
        }
        else
            super.onBackPressed();
    }
    public void trashNoteViewFragment(NoteModel trashNote){
        Bundle bundle=new Bundle();
        bundle.putParcelable("note",trashNote);
        Log.d(TAG, "noteViewFragment: ");
        TrashNoteItemFragment trashNoteItemFragment=new TrashNoteItemFragment();
        trashNoteItemFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.notes_fragment_container,trashNoteItemFragment).addToBackStack(null).commit();
        navigationView.getMenu().getItem(0).setChecked(false);
        navigationView.getMenu().getItem(1).setChecked(false);
    }
    public void noteViewFragment(NoteModel note){
        Bundle bundle=new Bundle();
        bundle.putParcelable("note",note);
        Log.d(TAG, "noteViewFragment: ");
        NoteItemFragment noteItemFragment=new NoteItemFragment();
        noteItemFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.notes_fragment_container,noteItemFragment).addToBackStack(null).commit();
        navigationView.getMenu().getItem(0).setChecked(false);
        navigationView.getMenu().getItem(1).setChecked(false);
    }


    @Override
    public void newNoteMessage() {
        Log.d(TAG, "newNoteMessage: ");
        getSupportFragmentManager().beginTransaction().replace(R.id.notes_fragment_container,new NewNoteFragment()).addToBackStack(null).commit();
        navigationView.getMenu().getItem(0).setChecked(false);
        navigationView.getMenu().getItem(1).setChecked(false);
    }


    @Override
    public void updateNote(NoteModel note) {
        NewNoteFragment newNoteFragment=new NewNoteFragment();
        Bundle bundle=new Bundle();
        bundle.putParcelable("note",note);
        newNoteFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.notes_fragment_container, newNoteFragment).commit();
    }

    public void setToolbartext(String my_notes) {
        Toolbar toolbar=findViewById(R.id.toolbar);
        TextView toolbar_title=toolbar.findViewById(R.id.toolbnar_title);
        toolbar_title.setTypeface(ResourcesCompat.getFont(this,R.font.montserratbold));
        toolbar_title.setText(my_notes);
    }


}