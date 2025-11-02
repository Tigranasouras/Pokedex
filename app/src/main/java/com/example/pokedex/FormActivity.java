package com.example.pokedex;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pokedex.R;
import com.example.pokedex.data.EntryContract;

public class FormActivity extends AppCompatActivity{
    private EditText etNum, etName, etSpecies, etAtk, etDef;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        etNum     = findViewById(R.id.et_number);
        etName    = findViewById(R.id.et_name);
        etSpecies = findViewById(R.id.et_species);
        etAtk     = findViewById(R.id.et_attack);
        etDef     = findViewById(R.id.et_defense);

        Button save = findViewById(R.id.btn_submit);
        save.setOnClickListener(v -> submit());

        //My extra credit feature!
        Button back = findViewById(R.id.btn_back);
        back.setOnClickListener(v -> finish());
    }

    private void submit() {
        String num = etNum.getText().toString().trim();
        String nm  = etName.getText().toString().trim();
        String sp  = etSpecies.getText().toString().trim();
        String a   = etAtk.getText().toString().trim();
        String d   = etDef.getText().toString().trim();

        if (num.isEmpty() || nm.isEmpty() || sp.isEmpty() || a.isEmpty() || d.isEmpty()) {
            Toast.makeText(this, "Fill all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // duplicate: same number+name+species
        String sel = EntryContract.Pokemon.COL_NUMBER + "=? AND " +
                EntryContract.Pokemon.COL_NAME + "=? AND " +
                EntryContract.Pokemon.COL_SPECIES + "=?";
        String[] args = { num, nm, sp };
        try (Cursor c = getContentResolver().query(EntryContract.CONTENT_URI, null, sel, args, null)) {
            if (c != null && c.getCount() > 0) {
                Toast.makeText(this, "Duplicate entry.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        ContentValues cv = new ContentValues();
        cv.put(EntryContract.Pokemon.COL_NUMBER,  num);
        cv.put(EntryContract.Pokemon.COL_NAME,    nm);
        cv.put(EntryContract.Pokemon.COL_SPECIES, sp);
        cv.put(EntryContract.Pokemon.COL_ATTACK,  Integer.parseInt(a));
        cv.put(EntryContract.Pokemon.COL_DEFENSE, Integer.parseInt(d));

        getContentResolver().insert(EntryContract.CONTENT_URI, cv);
        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();

        etNum.setText(""); etName.setText(""); etSpecies.setText("");
        etAtk.setText(""); etDef.setText("");
    }
}
