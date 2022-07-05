package com.example.notes.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notes.Model.NoteModel;
import com.example.notes.R;
import com.example.notes.ViewModels.HomeActivityandFragmentsViewModel;

import java.sql.Timestamp;
import java.util.Date;


public class NewNoteFragment extends Fragment {


    EditText title;
    EditText content;
    Button saveNote;
    HomeActivityandFragmentsViewModel viewModel;
    private String userId;
    NoteModel note;

    public NewNoteFragment() {
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
        View view = inflater.inflate(R.layout.fragment_new_note, container, false);
        title=view.findViewById(R.id.title);
        content=view.findViewById(R.id.content);
        saveNote=view.findViewById(R.id.save_note);
        title.setTypeface(ResourcesCompat.getFont(this.getContext(),R.font.montserratbold));
        content.setTypeface(ResourcesCompat.getFont(this.getContext(),R.font.montserratmedium));
        saveNote.setTypeface(ResourcesCompat.getFont(this.getContext(),R.font.montserratmedium));
        Bundle bundle=new Bundle();
        bundle=getArguments();
        if(bundle!=null){
            note = bundle.getParcelable("note");
            title.setText(note.getTitle());
            content.setText(note.getContent());
        }
        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(note!=null){
                    note.setTitle(title.getText().toString());
                    note.setContent(content.getText().toString());
                    note.setDate(new Timestamp(new Date().getTime()));
                    viewModel.addNote(note);
                    Toast.makeText(NewNoteFragment.this.getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.notes_fragment_container,new NotesFragment()).commit();

                }
                else {
                    viewModel.addNote(new NoteModel(title.getText().toString(),content.getText().toString(),false, new Timestamp(new Date().getTime())));
                }
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(HomeActivityandFragmentsViewModel.class);
        viewModel.setToolbartext(" ");
    }
}