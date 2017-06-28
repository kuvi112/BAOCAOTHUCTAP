package com.example.ongnauvi.nhacviec;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by OngNauVi on 27/06/2017.
 */

public class TKBDatabase extends SQLiteOpenHelper {

    private  static  final  String DATABASE_NAME ="DataTKB";

    private  static  final  String  TABLE_TKB = "TKB";

    private static  final String KEY_Thu ="Thu";
    private static  final String KEY_12="T12";
    private static  final String KEY_34="T34";
    private static  final String KEY_67="T67";
    private static  final String KEY_89="T89";
    private static  final String KEY_1112="T1112";
    private static  final String KEY_1314="T1314";

    private String CREATE_TKB_TABLE = "CREATE TABLE " + TABLE_TKB +
            "("
            + KEY_Thu + " INTEGER PRIMARY KEY,"
            + KEY_12 + " TEXT,"
            + KEY_34  + " TEXT,"
            + KEY_67 + " TEXT,"
            + KEY_89 + " TEXT,"
            + KEY_1112 + " TEXT,"
            + KEY_1314 + " TEXT " + ")";
    private String THEM_CAC_NGAY =
            "INSERT INTO " + TABLE_TKB + "VALUES('2',null,null,null,null,null,null)" +
            "INSERT INTO " + TABLE_TKB + "VALUES('3',null,null,null,null,null,null)" +
            "INSERT INTO " + TABLE_TKB + "VALUES('4',null,null,null,null,null,null)" +
            "INSERT INTO " + TABLE_TKB + "VALUES('5',null,null,null,null,null,null)" +
            "INSERT INTO " + TABLE_TKB + "VALUES('6',null,null,null,null,null,null)" +
            "INSERT INTO " + TABLE_TKB + "VALUES('7',null,null,null,null,null,null)";

    public TKBDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    //truy van khong tra ve KQ
    public void QueryData(String sql)
    {
        SQLiteDatabase database = getReadableDatabase();
        database.execSQL(sql);
    }
    //truy van co tra ve KQ
    public Cursor GetData(String sql)
    {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql,null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TKB_TABLE);
        db.execSQL(THEM_CAC_NGAY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void UpdateTKB(ClassTKB tkb)
    {
        SQLiteDatabase database = getWritableDatabase();
    }
}
