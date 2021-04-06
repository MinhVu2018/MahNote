package com.example.mahnote;

public class Note{
    public String note_title, note_tag, note_date, note_color, display_tag, contents;

    Note (String title, String tag, String date, String color){
        note_title = title;
        note_date = date;
        note_color = color;
        note_tag = tag;
        contents = "";
        generateDisplayTag();
    }

    public void generateDisplayTag(){
        String[] temp = note_tag.split("#");
        display_tag = "";
        for (int i = 1; i<temp.length; i++)
            display_tag += ("# " + temp[i] + "\n");
    }
}