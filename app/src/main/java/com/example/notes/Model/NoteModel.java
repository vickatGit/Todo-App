package com.example.notes.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.local.ReferenceSet;

import java.sql.Timestamp;
import java.util.Date;

public class NoteModel implements Parcelable {

    String noteId;
    String title;
    String content;
    boolean isTrash=false;
    Date date;
    String reference;

    public NoteModel(String title, String content, boolean isTrash, Date date) {
        this.title = title;
        this.content = content;
        this.isTrash = isTrash;
        this.date = date;

    }
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isTrash() {
        return isTrash;
    }

    public void setTrash(boolean trash) {
        isTrash = trash;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.noteId);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeByte(this.isTrash ? (byte) 1 : (byte) 0);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeString(this.reference);
    }

    public void readFromParcel(Parcel source) {
        this.noteId = source.readString();
        this.title = source.readString();
        this.content = source.readString();
        this.isTrash = source.readByte() != 0;
        long tmpDate = source.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.reference = source.readString();
    }

    protected NoteModel(Parcel in) {
        this.noteId = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.isTrash = in.readByte() != 0;
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.reference = in.readString();
    }

    public static final Parcelable.Creator<NoteModel> CREATOR = new Parcelable.Creator<NoteModel>() {
        @Override
        public NoteModel createFromParcel(Parcel source) {
            return new NoteModel(source);
        }

        @Override
        public NoteModel[] newArray(int size) {
            return new NoteModel[size];
        }
    };
}
