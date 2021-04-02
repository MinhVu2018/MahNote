package com.example.mahnote;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class NoteArrayAdapter extends ArrayAdapter<Note> {

    Context context;
    ArrayList<Note> arrayList;
    int layoutResource;

    public NoteArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Note> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResource = resource;
        this.arrayList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(layoutResource, null);

        TextView title = (TextView)convertView.findViewById(R.id.note_title);
        title.setText(arrayList.get(position).note_title);

        TextView tag = (TextView)convertView.findViewById(R.id.note_tag);
        tag.setText(arrayList.get(position).note_tag);

        TextView date = (TextView)convertView.findViewById(R.id.note_date);
        date.setText(arrayList.get(position).note_date);

        LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.note_layout);
        layout.setBackground(ContextCompat.getDrawable(context, R.drawable.frame_blue));
        layout.setPadding(15, 15, 15, 15);
        return convertView;
    }


}
