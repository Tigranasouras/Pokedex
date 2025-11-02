package com.example.pokedex;


import android.content.ContentUris;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pokedex.R;
import com.example.pokedex.data.EntryContract;

public class DbListActivity extends AppCompatActivity {
    //I see why you said this is the hardest thing yet
    private SimpleCursorAdapter adapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_list);

        ListView lv = findViewById(R.id.db_list);

        Cursor c = getContentResolver().query(
                EntryContract.CONTENT_URI, null, null, null, EntryContract.Pokemon.COL_NAME);

        String[] from = {
                EntryContract.Pokemon.COL_NAME,
                EntryContract.Pokemon.COL_SPECIES,
                EntryContract.Pokemon.COL_ATTACK,
                EntryContract.Pokemon.COL_DEFENSE
        };
        int[] to = { R.id.row_name, R.id.row_species, R.id.row_attack, R.id.row_defense };

        Button back = findViewById(R.id.btn_back);
        back.setOnClickListener(v -> finish());

        adapter = new SimpleCursorAdapter(this, R.layout.row_entry, c, from, to, 0);
        lv.setAdapter(adapter);

        lv.setOnItemLongClickListener((AdapterView<?> parent, android.view.View view, int pos, long id) -> {
            getContentResolver().delete(ContentUris.withAppendedId(EntryContract.CONTENT_URI, id), null, null);
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
            refresh();
            return true;
        });


    }

    private void refresh() {
        Cursor c = getContentResolver().query(
                EntryContract.CONTENT_URI, null, null, null, EntryContract.Pokemon.COL_NAME);
        adapter.changeCursor(c);
    }
}
