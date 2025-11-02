package com.example.pokedex;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText nationalInput, nameInput, speciesInput, heightInput, weightInput, hpInput, attackInput, defenseInput;
    Spinner spinner;
    RadioButton rbFemale, rbMale, rbUnknown;

    TextView nationalNumber, name, species, gender, height, weight, level, hp, attack, defense; //for red

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linearlayout);

        //this is so annoying - using all edit text has to be extra credit
        nationalInput = (EditText) findViewById(R.id.nationalinput);
        nameInput     = (EditText) findViewById(R.id.nameInput);
        speciesInput  = (EditText) findViewById(R.id.speciesInput);
        heightInput   = (EditText) findViewById(R.id.heightInput);
        weightInput   = (EditText) findViewById(R.id.weightInput);
        hpInput       = (EditText) findViewById(R.id.hpInput);
        attackInput   = (EditText) findViewById(R.id.attackInput);
        defenseInput  = (EditText) findViewById(R.id.defenseInput);
        spinner       = (Spinner)  findViewById(R.id.spinner);

        rbFemale = (RadioButton) findViewById(R.id.radioButton6);
        rbMale   = (RadioButton) findViewById(R.id.radioButton7);
        rbUnknown= (RadioButton) findViewById(R.id.radioButton8);

        nationalNumber = (TextView) findViewById(R.id.nationalNumber);
        name           = (TextView) findViewById(R.id.name);
        species        = (TextView) findViewById(R.id.species);
        gender         = (TextView) findViewById(R.id.gender);
        height         = (TextView) findViewById(R.id.height);
        weight         = (TextView) findViewById(R.id.weight);
        level          = (TextView) findViewById(R.id.level);
        hp             = (TextView) findViewById(R.id.hp);
        attack         = (TextView) findViewById(R.id.attack);
        defense        = (TextView) findViewById(R.id.defense);

        //spinner 1..50
        ArrayList<String> levels = new ArrayList<String>();
        for (int i = 1; i <= 50; i++) {
            levels.add(String.valueOf(i));
        }
        ArrayAdapter<String> a = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, levels);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(a);

        //this has to be extra credit
        setDefaults();

        Button btnReset = (Button) findViewById(R.id.button4);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setDefaults();
            }
        });

        Button btnSave = (Button) findViewById(R.id.button5);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                validateAndSave();
            }
        });

        findViewById(R.id.btn_open_form).setOnClickListener(v ->
                startActivity(new android.content.Intent(this, FormActivity.class)));

        findViewById(R.id.btn_open_list).setOnClickListener(v ->
                startActivity(new android.content.Intent(this, DbListActivity.class)));
    }

    private void setDefaults() {
        nationalNumber.setTextColor(Color.BLACK);
        name.setTextColor(Color.BLACK);
        species.setTextColor(Color.BLACK);
        gender.setTextColor(Color.BLACK);
        height.setTextColor(Color.BLACK);
        weight.setTextColor(Color.BLACK);
        level.setTextColor(Color.BLACK);
        hp.setTextColor(Color.BLACK);
        attack.setTextColor(Color.BLACK);
        defense.setTextColor(Color.BLACK);

        nationalInput.setText("896");
        nameInput.setText("Glastrier");
        speciesInput.setText("Wild Horse Pokemon");
        heightInput.setText("2.20");
        weightInput.setText("800.00");
        hpInput.setText("0");
        attackInput.setText("0");
        defenseInput.setText("0");

        rbFemale.setChecked(false);
        rbMale.setChecked(false);
        rbUnknown.setChecked(false);

        spinner.setSelection(0);

        Toast.makeText(this, "Form reset", Toast.LENGTH_SHORT).show();
    }

    private void validateAndSave() {
        setDefaultsLabelColorsOnly();

        boolean ok = true;
        String errors = "";

        // empty checks
        if (isEmpty(nationalInput)) { ok = false; nationalNumber.setTextColor(Color.RED); errors += "National Number is required\n"; }
        if (isEmpty(nameInput))     { ok = false; name.setTextColor(Color.RED);           errors += "Name is required\n"; }
        if (isEmpty(speciesInput))  { ok = false; species.setTextColor(Color.RED);        errors += "Species is required\n"; }
        if (isEmpty(heightInput))   { ok = false; height.setTextColor(Color.RED);         errors += "Height is required\n"; }
        if (isEmpty(weightInput))   { ok = false; weight.setTextColor(Color.RED);         errors += "Weight is required\n"; }
        if (isEmpty(hpInput))       { ok = false; hp.setTextColor(Color.RED);             errors += "HP is required\n"; }
        if (isEmpty(attackInput))   { ok = false; attack.setTextColor(Color.RED);         errors += "Attack is required\n"; }
        if (isEmpty(defenseInput))  { ok = false; defense.setTextColor(Color.RED);        errors += "Defense is required\n"; }

        //name 3-12 letters/spaces/dots - I hate java (haven't used it in a while)
        String nameStr = nameInput.getText().toString().trim();
        if (!nameStr.matches("(?i)^[a-z. ]{3,12}$")) {
            ok = false;
            name.setTextColor(Color.RED);
            errors += "Name must be 3–12 letters/spaces/dots\n";
        }

        //gender must be Male or Female (Unknown not allowed)
        if (!(rbMale.isChecked() || rbFemale.isChecked())) {
            ok = false;
            gender.setTextColor(Color.RED);
            errors += "Gender must be Male or Female\n";
        }

        //1-362
        Integer hpVal = toInt(hpInput);
        if (hpVal == null || hpVal.intValue() < 1 || hpVal.intValue() > 362) {
            ok = false; hp.setTextColor(Color.RED);
            errors += "HP must be between 1 and 362\n";
        }

        //Attack 0-526
        Integer atkVal = toInt(attackInput);
        if (atkVal == null || atkVal.intValue() < 0 || atkVal.intValue() > 526) {
            ok = false; attack.setTextColor(Color.RED);
            errors += "Attack must be between 0 and 526\n";
        }

        //Defense 10-614
        Integer defVal = toInt(defenseInput);
        if (defVal == null || defVal.intValue() < 10 || defVal.intValue() > 614) {
            ok = false; defense.setTextColor(Color.RED);
            errors += "Defense must be between 10 and 614\n";
        }

        //Height 0.20-169.99 w/ 2 decimals - java is annoying
        String hStr = heightInput.getText().toString().trim();
        if (!hStr.matches("^\\d{1,3}\\.\\d{2}$")) {
            ok = false; height.setTextColor(Color.RED);
            errors += "Height must have 2 decimals (e.g., 2.20)\n";
        } else {
            try {
                double h = Double.parseDouble(hStr);
                if (h < 0.20 || h > 169.99) {
                    ok = false; height.setTextColor(Color.RED);
                    errors += "Height must be between 0.20 and 169.99\n";
                }
            } catch (Exception ex) {
                ok = false; height.setTextColor(Color.RED);
                errors += "Height must be a number\n";
            }
        }

        //Weight 0.10-992.70 w/ 2 decimals
        String wStr = weightInput.getText().toString().trim();
        if (!wStr.matches("^\\d{1,3}\\.\\d{2}$")) {
            ok = false; weight.setTextColor(Color.RED);
            errors += "Weight must have 2 decimals (e.g., 800.00)\n";
        } else {
            try {
                double w = Double.parseDouble(wStr);
                if (w < 0.10 || w > 992.70) {
                    ok = false; weight.setTextColor(Color.RED);
                    errors += "Weight must be between 0.10 and 992.70\n";
                }
            } catch (Exception ex) {
                ok = false; weight.setTextColor(Color.RED);
                errors += "Weight must be a number\n";
            }
        }

        //National Number 0-1010
        Integer natVal = toInt(nationalInput);
        if (natVal == null || natVal.intValue() < 0 || natVal.intValue() > 1010) {
            ok = false; nationalNumber.setTextColor(Color.RED);
            errors += "National Number must be 0-1010\n";
        }

        if (ok) {
            Toast.makeText(this, "Saved to database (demo)", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, errors.trim(), Toast.LENGTH_LONG).show();
        }
    }

    //helpers to make my life less miserable - extra credit?
    private void setDefaultsLabelColorsOnly() {
        nationalNumber.setTextColor(Color.BLACK);
        name.setTextColor(Color.BLACK);
        species.setTextColor(Color.BLACK);
        gender.setTextColor(Color.BLACK);
        height.setTextColor(Color.BLACK);
        weight.setTextColor(Color.BLACK);
        level.setTextColor(Color.BLACK);
        hp.setTextColor(Color.BLACK);
        attack.setTextColor(Color.BLACK);
        defense.setTextColor(Color.BLACK);
    }

    private boolean isEmpty(EditText e) {
        return e.getText().toString().trim().isEmpty();
    }

    private Integer toInt(EditText e) {
        try {
            return Integer.valueOf(Integer.parseInt(e.getText().toString().trim()));
        } catch (Exception ex) {
            return null;
        }
    }


}
