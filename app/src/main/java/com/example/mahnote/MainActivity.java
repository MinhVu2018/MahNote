package com.example.mahnote;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import jp.wasabeef.richeditor.RichEditor;

public class MainActivity extends AppCompatActivity {
    LinearLayout note01,note02,note03,note04,note05,note06;
    ImageButton btn_search;
    RichEditor mEditor;
    LinearLayout left_layout, right_layout;
    ListView listview;
    ArrayList<Note> array;
    NoteArrayAdapter noteArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        setContentView(R.layout.activity_main);

        left_layout = findViewById(R.id.left_layout);
        right_layout = findViewById(R.id.right_layout);
//        listview = findViewById(R.id.listview);


        array = new ArrayList<Note>();
        array.add(new Note("Note_01", new String[]{"a","b","c"}, "30/03/2021"));
        array.add(new Note("Note_02", new String[]{"d","e","f"}, "30/03/2021"));
        array.add(new Note("Note_03", new String[]{"q","w","e","r"}, "30/03/2021"));

        noteArray = new NoteArrayAdapter(MainActivity.this, R.layout.activity_note, array);
//        listview.setAdapter(noteArray);



        setUp();
    }

    private void setUp(){
//        btn_search = findViewById(R.id.btn_search);
//        btn_search.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Toast.makeText(MainActivity.this, "search", Toast.LENGTH_LONG).show();
//            }
//        });

        note01 = findViewById(R.id.note_01);
        note01.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent new_intent = new Intent(MainActivity.this, WriteNote.class);
                new_intent.putExtra("note_name", "note_01");
                new_intent.putExtra("note_background", "skin");
                new_intent.putExtra("note_content", (Parcelable) mEditor);
                startActivity(new_intent);
            }
        });

        registerForContextMenu(note01);

        note02 = findViewById(R.id.note_02);
        note02.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent new_intent = new Intent(MainActivity.this, WriteNote.class);
                new_intent.putExtra("note_name", "note_02");
                new_intent.putExtra("note_background", "green");
                new_intent.putExtra("note_content", (Parcelable) mEditor);
                startActivity(new_intent);
            }
        });

        registerForContextMenu(note02);

        note03 = findViewById(R.id.note_03);
        note03.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent new_intent = new Intent(MainActivity.this, WriteNote.class);
                new_intent.putExtra("note_name", "note_03");
                new_intent.putExtra("note_background", "blue");
                new_intent.putExtra("note_content", (Parcelable) mEditor);
                startActivity(new_intent);
            }
        });

        registerForContextMenu(note03);

        note04 = findViewById(R.id.note_04);
        note04.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent new_intent = new Intent(MainActivity.this, WriteNote.class);
                new_intent.putExtra("note_name", "note_04");
                new_intent.putExtra("note_background", "pink");
                new_intent.putExtra("note_content", (Parcelable) mEditor);
                startActivity(new_intent);
            }
        });

        registerForContextMenu(note04);

        note05 = findViewById(R.id.note_05);
        note05.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent new_intent = new Intent(MainActivity.this, WriteNote.class);
                new_intent.putExtra("note_name", "note_05");
                new_intent.putExtra("note_background", "purple");
                new_intent.putExtra("note_content", (Parcelable) mEditor);
                startActivity(new_intent);
            }
        });

        registerForContextMenu(note05);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent new_intent = new Intent(MainActivity.this, WriteNote.class);
                new_intent.putExtra("note_name", "new note");
                new_intent.putExtra("note_background", "blue");
                new_intent.putExtra("note_content", (Parcelable) mEditor);
                startActivity(new_intent);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.note_option, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.option_remove:
                Toast.makeText(this, "Removed", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.option_01:
                Toast.makeText(this, "Hmm...", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search_option, menu);
        MenuItem menuItem = menu.findItem(R.id.searching);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search here!");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}