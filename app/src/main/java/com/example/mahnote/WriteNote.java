package com.example.mahnote;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import jp.wasabeef.richeditor.RichEditor;

public class WriteNote extends AppCompatActivity {
    EditText NoteTitle, NoteTag;
    TextView NoteDate;
    LinearLayout NoteContent;
    ImageButton btn_back, btn_save, btn_color;
    ConstraintLayout screen;

    private RichEditor mEditor;
    private Note curNote;
    private Boolean is_new, is_saved;
    private String oldTitle, curColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_write_note);

        NoteTitle = findViewById(R.id.note_name);
        NoteTag = findViewById(R.id.note_tag);
        NoteDate = findViewById(R.id.note_date);
        NoteContent = findViewById(R.id.note);
        screen = findViewById(R.id.note_background);

        mEditor = (RichEditor) findViewById(R.id.editor);
        is_new = false;
        is_saved = false;

        setUp();
    }

    private void setUp(){
        //get intent
        Bundle parameters = getIntent().getExtras();
        if (parameters != null) {   // existed note
            SharedPreferences shared = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
            Gson gson = new Gson();
            String temp_note = shared.getString(parameters.getString("note"), null);
            Type type = new TypeToken<Note>(){}.getType();
            curNote = gson.fromJson(temp_note, type);
        }
        else{ // new note
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            is_new = true;
            curNote = new Note("new_note", "#tag", currentDateandTime, "skincolor");
        }
        mEditor.setHtml(curNote.contents);
        NoteDate.setText("Last modified: " + curNote.note_date);
        NoteTitle.setText(curNote.note_title);
        NoteTag.setText(curNote.note_tag);
        NoteDate.setText(curNote.note_date);

        curColor = curNote.note_color;
        switch (curColor){
            case "skincolor":
                screen.setBackgroundColor(getResources().getColor(R.color.bg_skincolor));
                break;
            case "blue":
                screen.setBackgroundColor(getResources().getColor(R.color.bg_blue));
                break;
            case "green":
                screen.setBackgroundColor(getResources().getColor(R.color.bg_green));
                break;
            case "pink":
                screen.setBackgroundColor(getResources().getColor(R.color.bg_pink));
                break;
            case "purple":
                screen.setBackgroundColor(getResources().getColor(R.color.bg_purple));
                break;
            default:
                screen.setBackgroundColor(getResources().getColor(R.color.bg_blue));
                break;
        }

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent new_intent = new Intent(WriteNote.this, MainActivity.class);
                if (is_saved)
                    new_intent.putExtra("New", NoteTitle.getText().toString());
                if (!is_new)
                    new_intent.putExtra("Replace", oldTitle);
                startActivity(new_intent);
            }
        });

        btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());
                NoteDate.setText(currentDateandTime);

                is_saved = true;
                SharedPreferences shared = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                Gson gson = new Gson();

                curNote.note_color = curColor;
                curNote.note_date = currentDateandTime;
                curNote.note_tag = NoteTag.getText().toString();
                curNote.generateDisplayTag();
                curNote.contents = mEditor.getHtml();
                String curTitle = NoteTitle.getText().toString();
                if (!is_new && !curTitle.equals(curNote.note_title)){  // change existed note's title
                    editor.remove(curNote.note_title).apply();  // remove oldNote
                    oldTitle = curNote.note_title;
                }
                curNote.note_title = curTitle;          // change Note title
                editor.putString(curTitle, gson.toJson(curNote)); // add newNote
                editor.apply();
                Toast.makeText(WriteNote.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        });

        btn_color = findViewById(R.id.btn_color);
        btn_color.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showMenu(v);
            }
        });

        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder("Insert text here...");

        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.undo();
            }
        });

        findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.redo();
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBold();
            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setItalic();
            }
        });

        findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setSubscript();
            }
        });

        findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setSuperscript();
            }
        });

        findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setStrikeThrough();
            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

        findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(1);
            }
        });

        findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(2);
            }
        });

        findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(3);
            }
        });

        findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(4);
            }
        });

        findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(5);
            }
        });

        findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(6);
            }
        });

        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setIndent();
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setOutdent();
            }
        });

        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });

        findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBlockquote();
            }
        });

        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBullets();
            }
        });

        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setNumbers();
            }
        });

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertImage("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg",
                        "dachshund", 320);
            }
        });

        findViewById(R.id.action_insert_youtube).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertYoutubeVideo("https://www.youtube.com/embed/pS5peqApgUA");
            }
        });

        findViewById(R.id.action_insert_audio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertAudio("https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_5MG.mp3");
            }
        });

        findViewById(R.id.action_insert_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertVideo("https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/1080/Big_Buck_Bunny_1080_10s_10MB.mp4", 360);
            }
        });

        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertLink("https://github.com/wasabeef", "wasabeef");
            }
        });
        findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertTodo();
            }
        });
    }

    private void showMenu(View v){
        PopupMenu popupMenu = new PopupMenu(WriteNote.this, v);
        popupMenu.getMenuInflater().inflate(R.menu.color_option, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.green:
                        curColor = "green";
                        screen.setBackgroundColor(getResources().getColor(R.color.bg_green));
                        return true;
                    case R.id.blue:
                        curColor = "blue";
                        screen.setBackgroundColor(getResources().getColor(R.color.bg_blue));
                        return true;
                    case R.id.skin:
                        curColor = "skincolor";
                        screen.setBackgroundColor(getResources().getColor(R.color.bg_skincolor));
                        return true;
                    case R.id.pink:
                        curColor = "pink";
                        screen.setBackgroundColor(getResources().getColor(R.color.bg_pink));
                        return true;
                    case R.id.purple:
                        curColor = "purple";
                        screen.setBackgroundColor(getResources().getColor(R.color.bg_purple));
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

}