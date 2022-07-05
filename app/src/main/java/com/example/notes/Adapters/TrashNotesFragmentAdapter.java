package com.example.notes.Adapters;

import android.content.Context;
import android.util.Log;
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
import java.util.List;

public class TrashNotesFragmentAdapter extends RecyclerView.Adapter<TrashNotesFragmentAdapter.ListNoteItemHolder> {

    private static final String TAG = "tag";
    private final List<NoteModel> trashedNotes;
    private final HomeActivity homeActivity;
    private Context context;

    public TrashNotesFragmentAdapter(Context context, List<NoteModel> trashedNotes, HomeActivity parentActivity) {
        this.context=context;
        this.trashedNotes=trashedNotes;
        this.homeActivity=parentActivity;
    }

    @NonNull
    @Override
    public ListNoteItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new ListNoteItemHolder(layoutInflater.inflate(R.layout.trash_note_list_item_layout,parent,false));
    }

    @Override
    public int getItemCount() {
        return trashedNotes.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ListNoteItemHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        NoteModel note=trashedNotes.get(position);
        holder.title.setText(trashedNotes.get(position).getTitle());
        holder.content.setText(trashedNotes.get(position).getContent());
        holder.timestamp.setText(new SimpleDateFormat("dd/MM/yyyy").format(trashedNotes.get(position).getDate()));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.trashNoteViewFragment(trashedNotes.get(position));
            }
        });

    }

    public void updateAdapter(List<NoteModel> noteModels) {
        this.trashedNotes.clear();
        this.trashedNotes.addAll(noteModels);
        notifyDataSetChanged();
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
