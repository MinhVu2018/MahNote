package com.example.mahnote;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import jp.wasabeef.richeditor.RichEditor;

public class MainActivity extends AppCompatActivity {
    LinearLayout note01, note02;
    LinearLayout left_layout, right_layout;
    ListView list_note_view;
    ArrayList<Note> note_array;
    NoteArrayAdapter note_array_adapter;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        left_layout = findViewById(R.id.left_layout);
        right_layout = findViewById(R.id.right_layout);
        list_note_view = findViewById(R.id.listview);
        fab = findViewById(R.id.fab);

        note_array = new ArrayList<Note>();
        note_array_adapter = new NoteArrayAdapter(MainActivity.this, R.layout.activity_note, note_array);

        setUp();
    }

    private void setUp(){
        note_array.add(new Note("Note_01", new String[]{"a","b","c"}, "30/03/2021", "skincolor"));
        note_array.add(new Note("Note_02", new String[]{"d","e","f"}, "30/03/2021", "blue"));
        note_array.add(new Note("Note_03", new String[]{"q","w","e","r"}, "30/03/2021", "pink"));
        note_array.add(new Note("Note_01", new String[]{"a","b","c"}, "30/03/2021", "purple"));
        note_array.add(new Note("Note_02", new String[]{"d","e","f"}, "30/03/2021", "blue"));
        note_array.add(new Note("Note_03", new String[]{"q","w","e","r"}, "30/03/2021", "skincolor"));

        list_note_view.setAdapter(note_array_adapter);

        registerForContextMenu(list_note_view);

        list_note_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note selectedItem = (Note) parent.getItemAtPosition(position);

                Intent new_intent = new Intent(MainActivity.this, WriteNote.class);
                new_intent.putExtra("note_name", selectedItem.note_title);
                new_intent.putExtra("note_background", selectedItem.note_color);
                new_intent.putExtra("note_date", selectedItem.note_date);
                new_intent.putExtra("note_tag", selectedItem.note_tag);
                startActivity(new_intent);
            }
        });


        note01 = findViewById(R.id.note_01);
        note01.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent new_intent = new Intent(MainActivity.this, WriteNote.class);
                new_intent.putExtra("note_name", "note_01");
                new_intent.putExtra("note_background", "skin");
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
                startActivity(new_intent);
            }
        });

        registerForContextMenu(note02);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent new_intent = new Intent(MainActivity.this, WriteNote.class);
                new_intent.putExtra("note_name", "new note");
                new_intent.putExtra("note_background", "blue");
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

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}