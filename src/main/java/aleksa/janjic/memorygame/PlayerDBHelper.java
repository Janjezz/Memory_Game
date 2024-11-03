package aleksa.janjic.memorygame;

import android.content.Context;
import android.content.ContentValues;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PlayerDBHelper extends SQLiteOpenHelper {

    private final String TABLE_NAME = "games";
    public static final String COLUMN_ID = "game_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_POINTS = "score";

    public PlayerDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " TEXT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_POINTS + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void delete(String username){
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_NAME, COLUMN_USERNAME + " =?", new String[] {username});
        close();
    }

    public void deleteAll(){
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_NAME, null, null);
    }

    public void insert(Element element){
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, element.getmId());
        values.put(COLUMN_USERNAME, element.getmIme());
        values.put(COLUMN_EMAIL, element.getmEmail());
        values.put(COLUMN_POINTS, element.getmRezultat());
        database.insert(TABLE_NAME, null, values);
        close();
    }

    public int[] getRezults(String username){
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, COLUMN_USERNAME + " =?", new String[]{username}, null, null, "CAST(" + COLUMN_POINTS + " AS INTEGER)" + " DESC", null);
        if(cursor.getCount() <= 0){
            return null;
        }
        int[] results = new int[cursor.getCount()];
        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            results[i++] = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_POINTS));
        }
        return results;
    }

    public Element createElement(Cursor cursor){
        String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
        String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
        int[] results = getRezults(username);
        String bResult = String.valueOf(results[0]);
        String wResult = String.valueOf(results[results.length - 1]);
        Element e = new Element(username, email, bResult, wResult, true);
        return e;
    }

    public Element[] readPlayers(){
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(true, TABLE_NAME, new String[]{COLUMN_USERNAME, COLUMN_EMAIL, COLUMN_ID, COLUMN_POINTS}, null, null, COLUMN_USERNAME, null, null, null);

        if(cursor.getCount() <= 0){
            return null;
        }
        Element[] elements = new Element[cursor.getCount()];
        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            elements[i++] = createElement(cursor);
        }
        close();
        return elements;
    }

}
