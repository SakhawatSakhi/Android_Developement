package com.SakhiCoder.attendanceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.security.SecureRandom;

public class LoginDBHelper extends SQLiteOpenHelper {

    //Start Register and Login Database
    public static final String DBNAME= "Attendance_Login.db";
    public LoginDBHelper(@Nullable Context context) {
        super(context, "Login.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(username TEXT primary key,password TEXT,email TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
     MyDB.execSQL("drop Table if exists users");
    }
    public Boolean insertData(String username,String password,String email){
        SQLiteDatabase MyDB=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("password",password);
        contentValues.put("email",email);
        long result = MyDB.insert("users",null,contentValues);
        if (result==-1) return false;
        else
            return true;
    }
    public Boolean checkusername(String username){
        SQLiteDatabase MyDB =this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?",new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }
    public Boolean checkemail(String email){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor= MyDB.rawQuery("Select * from users where email= ? ",new String[]{email});
        if (cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public Boolean checkusernamepassword(String username,String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor= MyDB.rawQuery("Select * from users where username= ? and password= ?",new String[]{username,password});
        if (cursor.getCount()>0)
            return true;
        else
            return false;
    }
//    public void generateResetToken(String username) {
//        SQLiteDatabase MyDB = this.getWritableDatabase();
//        String resetToken = generateRandomToken(); // Implement this method to generate a random token
//        ContentValues values = new ContentValues();
//        values.put("reset_token", resetToken);
//        MyDB.update("users", values, "username=?", new String[]{username});
//    }
//
//
//    public void resetPassword(String username, String resetToken, String newPassword) {
//        SQLiteDatabase MyDB = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("password", newPassword);
//        values.put("reset_token", (String) null); // Reset the reset token after using it
//        MyDB.update("users", values, "username=? AND reset_token=?", new String[]{username, resetToken});
//    }


}//End Login and Registration Database
