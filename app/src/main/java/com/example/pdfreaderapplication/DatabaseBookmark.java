package com.example.pdfreaderapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pdfreaderapplication.entity.BookMarkEntity;

import java.util.ArrayList;
import java.util.List;

public class DatabaseBookmark extends SQLiteOpenHelper {
    public static  final  String DATABASE_NAME = "Database_bookmark_pdf_file.db";
    public static final String Table_Name = "Table_bookmark_pdf_file";
    public static  final String Col_1 = "ID";
    public static  final String Col_2 = "Pdf_bookmark_Files_data";

    public DatabaseBookmark(Context context) {
        super(context, DATABASE_NAME, null, 3);
        SQLiteDatabase db=this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Table_Name + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,Pdf_bookmark_Files_data TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        onCreate(db);
    }
    public boolean insertBookData(String Pdf_bookmark_Files){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_2,Pdf_bookmark_Files);
        long result=  db.insert(Table_Name,null,contentValues);
        if(result==-1) return false;
        else return true;
    }

    public  ArrayList<BookMarkEntity>  getAllBookdata(){
        ArrayList<BookMarkEntity> tList = new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from " + Table_Name,null);
        while (res!=null && res.moveToNext()) {
           BookMarkEntity b = new BookMarkEntity(res.getInt(0),res.getString(1));
            tList.add(b);
        }

        return tList;
    }

   /* public boolean DeleteHBookmarkData(int ID){
        SQLiteDatabase db=getWritableDatabase();
        long res=db.delete(Table_Name,Col_1+"="+ID,null);
        if(res==-1)return false;
        else return true;
    }*/

    public boolean DeleteHBookmarkDataNew(String  fileName){
        SQLiteDatabase db=getWritableDatabase();
        long res=db.delete(Table_Name,Col_2+" = '"+fileName+"'",null);
        if(res==-1)return false;
        else return true;
    }
}