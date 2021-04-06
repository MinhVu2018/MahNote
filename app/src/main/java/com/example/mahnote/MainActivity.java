package com.example.mahnote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

import jp.wasabeef.richeditor.RichEditor;

public class MainActivity extends AppCompatActivity {
    ListView left_list_note_view, right_list_note_view, selected_list_view;
    View clickSource, touchSource;
    int offset = 0;
    int left_height, right_height;
    ArrayList<String> left_note_name, right_note_name;
    ArrayList<Note> left_note_array, right_note_array;
    NoteArrayAdapter left_note_array_adapter, right_note_array_adapter, selected_array_adapter;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        left_list_note_view = findViewById(R.id.leftlistview);
        right_list_note_view = findViewById(R.id.rightlistview);
        fab = findViewById(R.id.fab);

        left_note_array = new ArrayList<Note>();
        right_note_array = new ArrayList<Note>();
        loadData();
        left_note_array_adapter = new NoteArrayAdapter(MainActivity.this, R.layout.activity_note, left_note_array);
        right_note_array_adapter = new NoteArrayAdapter(MainActivity.this, R.layout.activity_note, right_note_array);

        setUp();
    }

    private void setUp(){
        left_list_note_view.setAdapter(left_note_array_adapter);
        right_list_note_view.setAdapter(right_note_array_adapter);

        registerForContextMenu(left_list_note_view);
        registerForContextMenu(right_list_note_view);

        left_list_note_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note selectedItem = (Note) parent.getItemAtPosition(position);

                Intent new_intent = new Intent(MainActivity.this, WriteNote.class);
                new_intent.putExtra("note", selectedItem.note_title);
                startActivity(new_intent);
            }
        });

        left_list_note_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(touchSource == null)
                    touchSource = v;

                if(v == touchSource) {
                    right_list_note_view.dispatchTouchEvent(event);
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        clickSource = v;
                        touchSource = null;
                    }
                }

                return false;
            }
        });

        left_list_note_view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(view == clickSource)
                    if (view.getChildAt(0) != null)
                        left_list_note_view.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset);
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
        });

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // Set listView's x, y coordinates in loc[0], loc[1]
                int[] loc = new int[2];
                left_list_note_view.getLocationInWindow(loc);

                // Save listView's y and get listView2's coordinates
                int firstY = loc[1];
                right_list_note_view.getLocationInWindow(loc);

                offset = firstY - loc[1];
                //Log.v("Example", "offset: " + offset + " = " + firstY + " + " + loc[1]);
            }
        };

        right_list_note_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note selectedItem = (Note) parent.getItemAtPosition(position);

                Intent new_intent = new Intent(MainActivity.this, WriteNote.class);
                new_intent.putExtra("note", selectedItem.note_title);
                startActivity(new_intent);
            }
        });

        right_list_note_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(touchSource == null)
                    touchSource = v;

                if(v == touchSource) {
                    left_list_note_view.dispatchTouchEvent(event);
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        clickSource = v;
                        touchSource = null;
                    }
                }

                return false;
            }
        });

        right_list_note_view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(view == clickSource)
                    if (view.getChildAt(0) != null)
                        right_list_note_view.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset);
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
        });

        Handler handler2 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // Set listView's x, y coordinates in loc[0], loc[1]
                int[] loc = new int[2];
                right_list_note_view.getLocationInWindow(loc);

                // Save listView's y and get listView2's coordinates
                int firstY = loc[1];
                left_list_note_view.getLocationInWindow(loc);

                offset = firstY - loc[1];
                //Log.v("Example", "offset: " + offset + " = " + firstY + " + " + loc[1]);
            }
        };

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent new_intent = new Intent(MainActivity.this, WriteNote.class);
                startActivity(new_intent);
            }
        });

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            SharedPreferences shared = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();
            Gson gson = new Gson();

            String temp_note = shared.getString(extras.getString("New"), null);
            Type type = new TypeToken<Note>(){}.getType();
            Note temp = gson.fromJson(temp_note, type);

            String name_place = extras.getString("Replace");
            if (name_place != null)
                replaceNote(name_place, temp.note_title);
            else if (temp != null && !checkExist(temp.note_title))
                newNote(temp);

            saveData();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.note_option, menu);

        try {
            selected_list_view = (ListView) v;
            selected_array_adapter = (NoteArrayAdapter)selected_list_view.getAdapter();
        } catch(ClassCastException e) {
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.option_remove:
                String name = selected_array_adapter.getItem(info.position).note_title;
                deleteNote(name);
                saveData();

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
                ArrayList<Note> result = new ArrayList<Note>();
                for(Note x : left_note_array){
                    if (!result.contains(x) && (x.note_tag.contains(newText) || x.contents.contains(newText)))
                        result.add(x);
                }
                for(Note x : right_note_array){
                    if (!result.contains(x) && (x.note_tag.contains(newText) || x.contents.contains(newText)))
                        result.add(x);
                }
                if (result.size() == 0)
                    return false;
                ArrayList<Note> right_result = new ArrayList<Note>(result.subList(0,result.size()/2));
                ArrayList<Note> left_result = new ArrayList<Note> (result.subList(result.size()/2, result.size()));

                left_note_array_adapter.update(left_result);
                right_note_array_adapter.update(right_result);

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public static int getTotalHeightofListView(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();

        int totalHeight = 0;

        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);

            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),

                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            totalHeight += mView.getMeasuredHeight();
            Log.w("HEIGHT" + i, String.valueOf(totalHeight));

        }

        return totalHeight;
    }

    private void saveData(){
        SharedPreferences shared = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        Gson gson = new Gson();

        editor.putString("left_array", gson.toJson(left_note_name));
        editor.putString("right_array", gson.toJson(right_note_name));

        editor.apply();
    }

    private void loadData(){
        SharedPreferences shared = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);

        Gson gson = new Gson();
        String json_left = shared.getString("left_array", null);
        String json_right = shared.getString("right_array", null);

        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        left_note_name = gson.fromJson(json_left, type);
        if (left_note_name == null)
            left_note_name = new ArrayList<String>();
        else{
            for (int i=0; i<left_note_name.size(); i++){
                String temp_name = shared.getString(left_note_name.get(i), null);
                Note temp_note = gson.fromJson(temp_name, new TypeToken<Note>(){}.getType());
                left_note_array.add(temp_note);
            }
        }

        right_note_name = gson.fromJson(json_right, type);
        if (right_note_name == null)
            right_note_name = new ArrayList<String>();
        else{
            for (int i=0; i<right_note_name.size(); i++){
                String temp_name = shared.getString(right_note_name.get(i), null);
                Note temp_note = gson.fromJson(temp_name, new TypeToken<Note>(){}.getType());
                right_note_array.add(temp_note);
            }
        }
    }

    private void newNote(Note a){
        left_height = getTotalHeightofListView(left_list_note_view);
        right_height = getTotalHeightofListView(right_list_note_view);
//        Toast.makeText(MainActivity.this, Integer.toString(left_height) + ", " + Integer.toString(right_height) , Toast.LENGTH_SHORT).show();
        if (left_height > right_height) {
            right_note_name.add(a.note_title);
            right_note_array.add(a);
            right_note_array_adapter.notifyDataSetChanged();
        }
        else{
            left_note_name.add(a.note_title);
            left_note_array.add(a);
            left_note_array_adapter.notifyDataSetChanged();
        }
    }

    private void replaceNote(String old_name, String new_name)  {
        for(int i=0; i<left_note_name.size(); i++)
            if (old_name.equals(left_note_name.get(i))) {
                left_note_name.set(i, new_name);
                left_note_array_adapter.notifyDataSetChanged();
                return;
            }

        for (int i=0; i<right_note_name.size(); i++)
            if (old_name.equals(right_note_name.get(i))){
                right_note_name.set(i, new_name);
                right_note_array_adapter.notifyDataSetChanged();
                return;
            }
    }

    private void deleteNote(String name){
        for(int i=0; i<left_note_name.size(); i++)
            if (name.equals(left_note_name.get(i))){
                left_note_name.remove(i);
                left_note_array.remove(i);
                left_note_array_adapter.notifyDataSetChanged();
                break;
            }
        for(int i=0; i<right_note_name.size(); i++)
            if (name.equals(right_note_name.get(i))){
                right_note_name.remove(i);
                right_note_array.remove(i);
                right_note_array_adapter.notifyDataSetChanged();
                break;
            }
        SharedPreferences shared = getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.remove(name).apply();  // remove Note
    }

    private boolean checkExist(String name){
        for (String n:left_note_name)
            if(name.equals(n))
                return true;
        for (String n:right_note_name)
            if(name.equals(n))
                return true;
        return false;
    }
}