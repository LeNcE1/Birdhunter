package com.example.lence.bird_hunter.dateBase;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lence.bird_hunter.ui.MVPDB;
import com.example.lence.bird_hunter.ui.MVPUpDate;
import com.example.lence.bird_hunter.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class DBManager implements MVPDB {
    DB db;
    SQLiteDatabase database;
    MVPUpDate mvpUpDate;

    public DBManager(MainActivity mvpUpDate) {
        db = new DB(mvpUpDate);
        this.mvpUpDate = mvpUpDate;
    }

//    @Override
//    public void insert(String bird) {
//        database = db.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(DB.KEY_NAME, bird);
//        database.insert(DB.TABLE_BIRDS, null, contentValues);
//        //mvpUpDate.showNewUser();
//    }

    @Override
    public List<String> getBirds() {
        database = db.getReadableDatabase();
        List<String> rez = new ArrayList<>();
        Cursor cursor = database.query(DB.TABLE_BIRDS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int key_id = cursor.getColumnIndex(DB.KEY_BIRD_ID);
            int key_name = cursor.getColumnIndex(DB.KEY_NAME);
            do {

                rez.add(cursor.getString(key_name));
            }
            while (cursor.moveToNext());
        } else {
            return rez;
        }
        return rez;
    }

//    @Override
//    public void delete(String id) {
//        database = db.getReadableDatabase();
//        database.delete(DB.TABLE_USER, DB.KEY_USER_ID + " =?", new String[]{id});
//        mvpUpDate.showNewUser();
//    }

    @Override
    public void upDate(ArrayList<String> birds) {
        database = db.getReadableDatabase();
        database.delete(DB.TABLE_BIRDS,null,null);
        //db = new DB((Context) mvpUpDate);

        for(String i:birds) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DB.KEY_NAME, i);
            database.insert(DB.TABLE_BIRDS,null,contentValues);
        }
//        int id=0;
//        database.update(DB.TABLE_BIRDS, contentValues, DB.KEY_BIRD_ID + " = " + (id++), null);
        //mvpUpDate.showNewUser();
    }
}
