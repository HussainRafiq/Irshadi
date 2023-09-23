package com.hussain.irshadi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class sqldb extends SQLiteOpenHelper {
    SQLiteDatabase db ;
    public sqldb(Context context) {
        super(context, "irshadi", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table favourite (id INTEGER primary key,postid varchar(100))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS favourite");
    }
    public void insertfav(String postid) {
try {
    db = this.getWritableDatabase();

    db.execSQL("insert into favourite values(0,'" + postid + "')");
}catch (Exception ex){


        }
    }

    public Cursor getfav() {
        db=this.getWritableDatabase();
        Cursor c=db.rawQuery("select distinct(postid) from favourite",null);

return c;
    }


    public void deletefav(String postid) {
        try {
            db = this.getWritableDatabase();

            db.execSQL("delete from favourite where postid='"+postid+"'");
        }catch (Exception ex){


        }
    }


}
