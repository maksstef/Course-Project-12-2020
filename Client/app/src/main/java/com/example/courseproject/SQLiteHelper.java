package com.example.courseproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void SignUp(String name, String login, String password, String email, String phone){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO users VALUES (NULL, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, name);
        statement.bindString(2, login);
        statement.bindString(3, password);
        statement.bindString(4, email);
        statement.bindString(5, phone);


        statement.executeInsert();
    }

    public void CreateEvent(int id, String title, String description, String date){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO events VALUES (NULL, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindLong(1, id);
        statement.bindString(2, title);
        statement.bindString(3, description);
        statement.bindString(4, date);

        statement.executeInsert();
    }


    public void CreateMember(int e_id, int u_id){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO members VALUES ( ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindLong(1, e_id);
        statement.bindLong(2, u_id);

        statement.executeInsert();
    }

    public  void deleteEvent(Integer ID) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM events WHERE e_id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindLong(1, ID);

        statement.execute();
        database.close();
    }

    public  void deleteMembers(Integer ID) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM members WHERE e_id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindLong(1, ID);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
