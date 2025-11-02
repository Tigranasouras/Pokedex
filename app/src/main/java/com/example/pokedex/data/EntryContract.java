package com.example.pokedex.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class EntryContract {
    private EntryContract(){}

    public static final String AUTHORITY = "com.example.pokedex.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/pokemon");

    public static final class Pokemon implements BaseColumns {
        public static final String TABLE = "pokemon";
        public static final String COL_NUMBER = "national_number";
        public static final String COL_NAME = "name";
        public static final String COL_SPECIES = "species";
        public static final String COL_ATTACK = "attack";
        public static final String COL_DEFENSE = "defense";
    }

}
