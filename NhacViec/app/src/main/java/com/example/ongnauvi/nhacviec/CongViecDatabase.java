package com.example.ongnauvi.nhacviec;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OngNauVi on 05/06/2017.
 */

public class CongViecDatabase extends SQLiteOpenHelper {

    //
    private static final int DATABASE_VERSION = 1;
    // ten csdl
    private  static  final  String DATABASE_NAME ="DataCongviec";
    // ten bang
    private  static  final  String  TABLE_CV = "TCongviec";
    // các trường trong bảng
    private static  final String KEY_ID ="MaCV";
    private static  final String KEY_TENCV="TenCV";
    private static  final String KEY_MOTA="Mota";
    private static  final String KEY_NGAY="Ngay";
    private static  final String KEY_GIO="Gio";
    private static  final String KEY_KIEULAPLAI="Kieulaplai";
    private static  final String KEY_TRANGTHAI="Trangthai";

    // private Context context;
    public CongViecDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        //this.context=context;
    }
    // truy vấn tạo bảng, sửa xóa.....

    public  void QueryData (String sql)
    {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //tao bang
        String CREATE_CONGVIEC_TABLE = "CREATE TABLE " + TABLE_CV +
                "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TENCV + " TEXT,"
                + KEY_MOTA  + " TEXT,"
                + KEY_NGAY + " TEXT,"
                + KEY_GIO + " TEXT,"
                + KEY_KIEULAPLAI + " TEXT,"
                + KEY_TRANGTHAI + " BOOLEAN" + ")";
        db.execSQL(CREATE_CONGVIEC_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int ThemCV(ClassCV Cv)
    {
        SQLiteDatabase  db =this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_TENCV,Cv.getTencv());
        values.put(KEY_MOTA,Cv.getMota());
        values.put(KEY_NGAY,Cv.getNgay());
        values.put(KEY_GIO, Cv.getGio());
        values.put(KEY_KIEULAPLAI, Cv.getKieulaplai());
        values.put(KEY_TRANGTHAI, Cv.getTrangthai());

        long ID = db.insert(TABLE_CV, null, values);
        db.close();
        return (int) ID;
    }
    // DUA CAC CONG VIEC VAO 1 LIST
    public List<ClassCV> getAllCongViec()
    {
        List<ClassCV> congViecList = new ArrayList<>();
        String selectQuery=" SELECT * FROM "+ TABLE_CV;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do {
                ClassCV congViec =new ClassCV();
                congViec.setMacv(cursor.getInt(0));
                congViec.setTencv(cursor.getString(1));
                congViec.setMota(cursor.getString(2));
                congViec.setNgay(cursor.getString(3));
                congViec.setGio(cursor.getString(4));
                congViec.setKieulaplai(cursor.getString(5));
                congViec.setTrangthai(cursor.getString(6));
                congViecList.add(congViec);
            }while (cursor.moveToNext());
        }
        db.close();
        return congViecList;
    }

    //

    public ClassCV getCongViec(int ID)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CV, new String[]
                        {
                                KEY_ID,
                                KEY_TENCV,
                                KEY_MOTA,
                                KEY_NGAY,
                                KEY_GIO,
                                KEY_KIEULAPLAI,
                                KEY_TRANGTHAI
                        }, KEY_ID + "=?",

                new String[] {String.valueOf(ID)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        ClassCV CV = new ClassCV(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4),
                cursor.getString(5), cursor.getString(6));

        return CV;
    }
    // cap nhap mot cog viec lai
    public int updateCongViec (ClassCV congViec)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values =new ContentValues();

        values.put(KEY_TENCV , congViec.getTencv());
        values.put(KEY_MOTA, congViec.getMota());
        values.put(KEY_NGAY, congViec.getNgay());
        values.put(KEY_GIO, congViec.getGio());
        values.put(KEY_KIEULAPLAI, congViec.getKieulaplai());
        values.put(KEY_TRANGTHAI, congViec.getTrangthai());

        return db.update(TABLE_CV,values, KEY_ID + "=?", new String[] {String.valueOf(congViec.getMacv())});
    }
    // xoa mot cong viec
    public  void deleteCongViec(ClassCV congViec)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CV, KEY_ID + " =?",new String[] {String.valueOf(congViec.getMacv())});
    }
}
