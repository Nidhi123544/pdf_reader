package com.example.pdfreaderapplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pdfreaderapplication.entity.BookMarkEntity;
import com.example.pdfreaderapplication.entity.HistoryMarkEntity;

import java.io.File;
import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    public static  final  String DATABASE_NAME = "Database_pdf_file.db";
    public static final String Table_Name = "Table_pdf_file";
    public static  final String Col_1 = "ID";
    public static  final String Col_2 = "Pdf_Files_Data";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, 6);
        SQLiteDatabase db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Table_Name + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,Pdf_Files_Data TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        onCreate(db);
    }
    public boolean insertData(String Pdf_Files){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_2,Pdf_Files);
        long result=  db.insert(Table_Name,null,contentValues);
        if(result==-1) return false;
        else return true;
    }

    /*public Cursor getAlldata(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from " + Table_Name,null);
        return res;
    }*/


    public ArrayList<HistoryMarkEntity> getAllHistorydata(){
        ArrayList<HistoryMarkEntity> tList = new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from " + Table_Name,null);
        while (res!=null && res.moveToNext()) {
            HistoryMarkEntity b = new HistoryMarkEntity(res.getInt(0),res.getString(1));
            tList.add(b);
        }

        return tList;
    }

    public boolean DeleteHistoryData(int ID){
        SQLiteDatabase db=getWritableDatabase();
        long res=db.delete(Table_Name,Col_1+"="+ID,null);
        if(res==-1)return false;
        else return true;
    }

   /* public boolean UpdatePdfData(int id,String pdf_name){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Col_2,pdf_name);
        String whereArgs[]={""+id};
        int count=db.update(Table_Name,contentValues,Col_1+"=?",whereArgs);
        if (count==-1) return false;
        else return true;
    }*/

    public boolean deleteAllHistoryData(){
        SQLiteDatabase db=getWritableDatabase();
        long res=db.delete( Table_Name,null,null);
        if(res==-1) return false;
        else return true;
    }

    public boolean DeleteHistoryDataNew(String  fileName){
        SQLiteDatabase db=getWritableDatabase();
        long res=db.delete(Table_Name,Col_2+" = '"+fileName+"'",null);
        if(res==-1)return false;
        else return true;
    }
}