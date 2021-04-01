package com.example.mahnote;

public class Note{
    public String note_title, note_tag, note_date;

    Note (String title, String[] tag, String date){
        note_title = title;
        note_date = date;

        note_tag = "";
        for (String i : tag ) note_tag += ("# " + i + "\n");
    }

}