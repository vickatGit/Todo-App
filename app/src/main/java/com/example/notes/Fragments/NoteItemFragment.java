        package com.example.notes.Fragments;

import android.content.Context;
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

import com.example.notes.HomeActivity;
import com.example.notes.Model.NoteModel;
import com.example.notes.R;
import com.example.notes.ViewModels.HomeActivityandFragmentsViewModel;


public class NoteItemFragment extends Fragment {

    TextView title;
    TextView content;
    Button deleteNote;
    Button editNote;

    HomeActivity homeActivity;
    private HomeActivityandFragmentsViewModel viewModel;


    public NoteItemFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(HomeActivityandFragmentsViewModel.class);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof UpdateNote){
            this.homeActivity= (HomeActivity) context;
            homeActivity.setToolbartext(" ");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle=new Bundle();
        bundle=getArguments();
        NoteModel note=bundle.getParcelable("note");
        View view=inflater.inflate(R.layout.fragment_note_item, container, false);
        title=view.findViewById(R.id.trash_note_title);
        content=view.findViewById(R.id.note_content);
        deleteNote=view.findViewById(R.id.restore_note);
        editNote=view.findViewById(R.id.edit_note);
        title.setTypeface(ResourcesCompat.getFont(this.getContext(),R.font.montserratbold));
        content.setTypeface(ResourcesCompat.getFont(this.getContext(),R.font.montserratmedium));
        deleteNote.setTypeface(ResourcesCompat.getFont(this.getContext(),R.font.montserratmedium));
        editNote.setTypeface(ResourcesCompat.getFont(this.getContext(),R.font.montserratmedium));



        title.setText(note.getTitle());
        content.setText(note.getContent());
        deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.deleteNote(note.getNoteId());
                viewModel.fetchNotes();
                Toast.makeText(NoteItemFragment.this.getContext(),"Moved to Trash Successfully",Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });

        editNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.updateNote(note);
//                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }
    public interface UpdateNote{
        void updateNote(NoteModel note);
    }
}