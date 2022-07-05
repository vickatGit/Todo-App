package com.example.notes.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notes.Model.NoteModel;
import com.example.notes.R;
import com.example.notes.ViewModels.HomeActivityandFragmentsViewModel;


public class TrashNoteItemFragment extends Fragment {

    HomeActivityandFragmentsViewModel viewModel;


    public TrashNoteItemFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_trash_note_item, container, false);
        Bundle bundle=getArguments();
        NoteModel noteModel=bundle.getParcelable("note");
        TextView title=view.findViewById(R.id.trash_note_title);
        TextView content=view.findViewById(R.id.trash_note_content);
        Button restoreNoe=view.findViewById(R.id.restore_note);
        Button deleteNote=view.findViewById(R.id.delete_note_permanently);

        title.setTypeface(ResourcesCompat.getFont(this.getContext(),R.font.montserratbold));
        content.setTypeface(ResourcesCompat.getFont(this.getContext(),R.font.montserratmedium));
        restoreNoe.setTypeface(ResourcesCompat.getFont(this.getContext(),R.font.montserratmedium));

        title.setText(noteModel.getTitle());
        content.setText(noteModel.getContent());
        deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.deleteNotePermanently(noteModel.getNoteId());
                viewModel.fetchNotes();
                Toast.makeText(TrashNoteItemFragment.this.getContext(),"Note Deleted Successfully",Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        restoreNoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.restoreNote(noteModel.getNoteId());
                viewModel.fetchNotes();
                Toast.makeText(TrashNoteItemFragment.this.getContext(),"Restored Successfully",Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new  ViewModelProvider(getActivity()).get(HomeActivityandFragmentsViewModel.class);
        viewModel.setToolbartext(" ");

    }
}