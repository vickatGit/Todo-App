package com.example.notes.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.notes.Adapters.NotesFragmentAdapter;
import com.example.notes.HomeActivity;
import com.example.notes.Model.NoteModel;
import com.example.notes.R;
import com.example.notes.ViewModels.HomeActivityandFragmentsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotesFragment extends Fragment {

    private static final String TAG = "tag";
    RecyclerView recyclerView;
    HomeActivityandFragmentsViewModel viewModel;
    private HomeActivity homeActivity;
    List<NoteModel> allNotes=new ArrayList<>();
    NotesFragmentAdapter notesAdapter;

    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
        if(context instanceof newNoteMessage ){
            this.homeActivity= (HomeActivity) context;
            homeActivity.setToolbartext("My Notes");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        homeActivity.navigationView.getMenu().getItem(0).setChecked(true);

        View view=inflater.inflate(R.layout.fragment_notes, container, false);
        recyclerView=view.findViewById(R.id.all_notes);
        FloatingActionButton addNote=view.findViewById(R.id.floating_action_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        notesAdapter = new NotesFragmentAdapter(this.getContext(),allNotes,homeActivity);
        recyclerView.setAdapter(notesAdapter);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.newNoteMessage();
            }
        });
        return view;
    }
    public interface newNoteMessage{
        void newNoteMessage();
    }

    public NotesFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        viewModel = new ViewModelProvider(getActivity()).get(HomeActivityandFragmentsViewModel.class);
        viewModel.setToolbartext("My Notes");
        viewModel.setToolbarColor(true);
         viewModel.getNotes().observe(getViewLifecycleOwner(), new Observer<List<NoteModel>>() {

            @Override
            public void onChanged(List<NoteModel> noteModels) {
                Log.d(TAG, "onChanged: Notes"+noteModels.size());
                allNotes.clear();
                allNotes.addAll(noteModels);
                notesAdapter.updateAdapter(allNotes);
            }
        });

    }
}