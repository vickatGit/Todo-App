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

import com.example.notes.Adapters.TrashNotesFragmentAdapter;
import com.example.notes.HomeActivity;
import com.example.notes.Model.NoteModel;
import com.example.notes.R;
import com.example.notes.ViewModels.HomeActivityandFragmentsViewModel;

import java.util.ArrayList;
import java.util.List;

public class TrashedNotesFragment extends Fragment {

    private static final String TAG = "tag";
    private HomeActivityandFragmentsViewModel viewModel;
    List<NoteModel> trashedNotes=new ArrayList<>();
    RecyclerView trashNotesRecyclerView;
    TrashNotesFragmentAdapter trashNotesFragmentAdapter;
    private HomeActivity homeActivity;

    public TrashedNotesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof TrashedNote){
            homeActivity= (HomeActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeActivity.navigationView.getMenu().getItem(1).setChecked(true);
        View view=inflater.inflate(R.layout.fragment_trashed_notes, container, false);
        trashNotesRecyclerView = view.findViewById(R.id.trash_notes);


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(HomeActivityandFragmentsViewModel.class);
        viewModel.setToolbartext("Trashed Notes");
        viewModel.setToolbarColor(true);
        trashNotesFragmentAdapter = new TrashNotesFragmentAdapter(this.getContext(),trashedNotes,viewModel.getParentActivity());
        trashNotesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        trashNotesRecyclerView.setAdapter(trashNotesFragmentAdapter);
        viewModel.getTrashNotes().observe(getViewLifecycleOwner(), new Observer<List<NoteModel>>() {
            @Override
            public void onChanged(List<NoteModel> noteModels) {
                Log.d(TAG, "onChanged: "+noteModels.size());
                trashNotesFragmentAdapter.updateAdapter(noteModels);
            }
        });
    }

    public interface TrashedNote{
        void trashedNoteItem(NoteModel note);
    }

}