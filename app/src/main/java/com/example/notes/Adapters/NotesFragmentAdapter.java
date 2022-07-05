package com.example.notes.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.HomeActivity;
import com.example.notes.Model.NoteModel;
import com.example.notes.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class NotesFragmentAdapter extends RecyclerView.Adapter<NotesFragmentAdapter.ListNoteItemHolder> {

    private final List<NoteModel> notes=new ArrayList<>();
    private final HomeActivity homeActivity;
    private Context context;

    public NotesFragmentAdapter(Context context, List<NoteModel> allNotes, HomeActivity homeActivity) {
        this.context=context;
        notes.addAll(allNotes);
        this.homeActivity=homeActivity;
    }
    public void updateAdapter(List<NoteModel> notes){
        this.notes.clear();
        this.notes.addAll(notes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListNoteItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new ListNoteItemHolder(layoutInflater.inflate(R.layout.note_list_item_layout,parent,false));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ListNoteItemHolder holder, int position) {
        holder.title.setText(notes.get(position).getTitle());
        holder.content.setText(notes.get(position).getContent());
        holder.timestamp.setText(new SimpleDateFormat("dd/MM/yyyy").format(notes.get(position).getDate()));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.noteViewFragment(notes.get(position));
            }
        });

    }

    class ListNoteItemHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView content;
        TextView timestamp;
        View view;


        public ListNoteItemHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            title=itemView.findViewById(R.id.title);
            content=itemView.findViewById(R.id.content);
            timestamp=itemView.findViewById(R.id.timestamp);
            title.setTypeface(ResourcesCompat.getFont(context,R.font.montserratbold));
            content.setTypeface(ResourcesCompat.getFont(context,R.font.montserratmedium));
        }
    }
}
