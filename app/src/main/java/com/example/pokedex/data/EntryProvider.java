package com.example.pokedex.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import static com.example.pokedex.data.EntryContract.*;

public class EntryProvider extends ContentProvider{
 //geniuenly annoying to make this
    private static final int POKEMON    = 1;
    private static final int POKEMON_ID = 2;

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        MATCHER.addURI(AUTHORITY, "pokemon",   POKEMON);
        MATCHER.addURI(AUTHORITY, "pokemon/#", POKEMON_ID);
    }

    private SQLiteDatabase db;

    private static class DBHelper extends SQLiteOpenHelper {
        private static final String DB_NAME = "pokedex.db";
        private static final int DB_VERSION = 1;

        DBHelper(Context c){ super(c, DB_NAME, null, DB_VERSION); }

        @Override public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + EntryContract.Pokemon.TABLE + " (" +
                    EntryContract.Pokemon._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    EntryContract.Pokemon.COL_NUMBER  + " TEXT, " +
                    EntryContract.Pokemon.COL_NAME    + " TEXT, " +
                    EntryContract.Pokemon.COL_SPECIES + " TEXT, " +
                    EntryContract.Pokemon.COL_ATTACK  + " INTEGER, " +
                    EntryContract.Pokemon.COL_DEFENSE + " INTEGER)");
        }

        @Override public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
            db.execSQL("DROP TABLE IF EXISTS " + EntryContract.Pokemon.TABLE);
            onCreate(db);
        }
    }

    @Override public boolean onCreate() {
        db = new DBHelper(getContext()).getWritableDatabase();
        return db != null;
    }

    @Override public Cursor query(Uri uri, String[] proj, String sel, String[] selArgs, String sort) {
        int code = MATCHER.match(uri);
        Cursor c;
        if (code == POKEMON) {
            c = db.query(Pokemon.TABLE, proj, sel, selArgs, null, null, sort);
        } else if (code == POKEMON_ID) {
            String where = Pokemon._ID + "=?";
            String[] args = new String[]{ String.valueOf(ContentUris.parseId(uri)) };
            c = db.query(Pokemon.TABLE, proj, where, args, null, null, sort);
        } else {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override public Uri insert(Uri uri, ContentValues values) {
        if (MATCHER.match(uri) != POKEMON) throw new IllegalArgumentException("Bad URI " + uri);
        long id = db.insert(Pokemon.TABLE, null, values);
        if (id <= 0) throw new SQLException("Insert failed");
        Uri newUri = ContentUris.withAppendedId(CONTENT_URI, id);
        getContext().getContentResolver().notifyChange(newUri, null);
        return newUri;
    }

    @Override public int delete(Uri uri, String sel, String[] selArgs) {
        int code = MATCHER.match(uri);
        int count;
        if (code == POKEMON) {
            count = db.delete(Pokemon.TABLE, sel, selArgs);
        } else if (code == POKEMON_ID) {
            String where = Pokemon._ID + "=?";
            String[] args = new String[]{ String.valueOf(ContentUris.parseId(uri)) };
            count = db.delete(Pokemon.TABLE, where, args);
        } else throw new IllegalArgumentException("Unknown URI " + uri);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override public int update(Uri uri, ContentValues v, String s, String[] a) { return 0; }
    @Override public String getType(Uri uri) {
        int code = MATCHER.match(uri);
        if (code == POKEMON)    return "vnd.android.cursor.dir/vnd."  + AUTHORITY + ".pokemon";
        if (code == POKEMON_ID) return "vnd.android.cursor.item/vnd." + AUTHORITY + ".pokemon";
        throw new IllegalArgumentException("Unknown URI " + uri);
    }
}
