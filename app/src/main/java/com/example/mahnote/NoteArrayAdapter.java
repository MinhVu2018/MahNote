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
    ArrayList<Note> arrayList = new ArrayList<>();
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
        tag.setText(arrayList.get(position).display_tag);

        TextView date = (TextView)convertView.findViewById(R.id.note_date);
        date.setText(arrayList.get(position).note_date);

        LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.note_layout);
        switch (arrayList.get(position).note_color){
            case "skincolor":
                layout.setBackground(ContextCompat.getDrawable(context, R.drawable.frame_skincolor));
                break;
            case "blue":
                layout.setBackground(ContextCompat.getDrawable(context, R.drawable.frame_blue));
                break;
            case "green":
                layout.setBackground(ContextCompat.getDrawable(context, R.drawable.frame_green));
                break;
            case "pink":
                layout.setBackground(ContextCompat.getDrawable(context, R.drawable.frame_pink));
                break;
            case "purple":
                layout.setBackground(ContextCompat.getDrawable(context, R.drawable.frame_purple));
                break;
            default:
                break;
        }
        layout.setPadding(15, 15, 15, 15);
        return convertView;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    public void update(ArrayList<Note> temp_list){
        arrayList.clear();
        arrayList.addAll(temp_list);
        notifyDataSetChanged();
    }
}
