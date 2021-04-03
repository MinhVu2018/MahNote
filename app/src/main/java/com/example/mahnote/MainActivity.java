package com.example.mahnote;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import jp.wasabeef.richeditor.RichEditor;

public class MainActivity extends AppCompatActivity {
    ListView left_list_note_view, right_list_note_view;
    View clickSource, touchSource;
    int offset = 0;
    int left_height, right_height;
    ArrayList<Note> left_note_array, right_note_array;
    NoteArrayAdapter left_note_array_adapter, right_note_array_adapter;
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
        left_note_array_adapter = new NoteArrayAdapter(MainActivity.this, R.layout.activity_note, left_note_array);
        right_note_array_adapter = new NoteArrayAdapter(MainActivity.this, R.layout.activity_note, right_note_array);

        setUp();
    }

    private void setUp(){
        left_note_array.add(new Note("Note_01", new String[]{"a","b","c"}, "30/03/2021", "skincolor"));
        left_note_array.add(new Note("Note_02", new String[]{"d","e","f"}, "30/03/2021", "blue"));
        left_note_array.add(new Note("Note_03", new String[]{"q","w","e","r"}, "30/03/2021", "pink"));
        left_note_array.add(new Note("Note_04", new String[]{"b","t","e","x"}, "30/03/2021", "green"));
        left_note_array.add(new Note("Note_05", new String[]{"b","t","e","x"}, "30/03/2021", "blue"));
        right_note_array.add(new Note("Note_01", new String[]{"a","b","c"}, "30/03/2021", "purple"));
        right_note_array.add(new Note("Note_02", new String[]{"d","e","f"}, "30/03/2021", "blue"));
        right_note_array.add(new Note("Note_03", new String[]{"q","w","e","r"}, "30/03/2021", "skincolor"));
        right_note_array.add(new Note("Note_04", new String[]{"q","w","e","r","d","e","f"}, "30/03/2021", "skincolor"));

        left_list_note_view.setAdapter(left_note_array_adapter);
        right_list_note_view.setAdapter(right_note_array_adapter);

        registerForContextMenu(left_list_note_view);
        registerForContextMenu(right_list_note_view);

        left_list_note_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                new_intent.putExtra("note_name", selectedItem.note_title);
                new_intent.putExtra("note_background", selectedItem.note_color);
                new_intent.putExtra("note_date", selectedItem.note_date);
                new_intent.putExtra("note_tag", selectedItem.note_tag);
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
//                Intent new_intent = new Intent(MainActivity.this, WriteNote.class);
//                new_intent.putExtra("note_name", "new note");
//                new_intent.putExtra("note_background", "blue");
//                startActivity(new_intent);

                left_height = getTotalHeightofListView(left_list_note_view);
                right_height = getTotalHeightofListView(right_list_note_view);
                Toast.makeText(MainActivity.this, Integer.toString(left_height) + ", " + Integer.toString(right_height) , Toast.LENGTH_SHORT).show();
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
}