package com.example.ongnauvi.nhacviec;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.ongnauvi.nhacviec.R.id.save;
import static com.example.ongnauvi.nhacviec.R.id.txtNgay;

/**
 * Created by OngNauVi on 22/06/2017.
 */

public class ChinhSuaCV extends AppCompatActivity{
    private Toolbar toolbar;
    private EditText TieuDeText,MoTaText;
    private TextView NgayText, GioText, LapText;
    private String mTieuDe;
    private String mNgay;
    private String mGio;
    private String mMoTa;
    private String mKieuLapLai;
    private String mTrangThai;
    private String[] mDateSplit;
    private String[] mTimeSplit;
    private int mYear, mMonth, mHour, mMinute, mDay;
    private int ReceiverID;
    private CongViecDatabase db;
    private ClassCV ReceivedCV;
    private Calendar calendar;
    private AlarmReceiver alarmReceiver;
    private Button btnChonGio,btnChonNgay,btnLap;

    private long Repeat;


    //chuoi co dinh
    public static  final String KEY_ID ="MaCV_Key";

    private static  final String KEY_TENCV="TenCV_Key";
    private static  final String KEY_MOTA="Mota_Key";
    private static  final String KEY_NGAY="Ngay_Key";
    private static  final String KEY_GIO="Gio_Key";
    private static  final String KEY_KIEULAPLAI="Kieulaplai_Key";
    private static  final String KEY_TRANGTHAI="Trangthai_Key";

    // Giá trị hằng số bằng mili giây
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_cv);

        //Anh Xa
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        TieuDeText = (EditText)findViewById(R.id.editTenCv);
        MoTaText = (EditText) findViewById(R.id.editMt);
        NgayText = (TextView)findViewById(txtNgay) ;
        GioText = (TextView)findViewById(R.id.txtGio);
        LapText = (TextView) findViewById(R.id.txtKieuLap);
        btnChonGio = (Button)findViewById(R.id.BtnGio);
        btnChonNgay = (Button)findViewById(R.id.BtnNgay);
        btnLap = (Button)findViewById(R.id.BtnLap);

        //toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sửa Công Việc");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        //Thiet lap Cong Viec
        TieuDeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mTieuDe = s.toString().trim();
                TieuDeText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        //nhan ID
        ReceiverID = Integer.parseInt(getIntent().getStringExtra(KEY_ID));

        //nhan phan con lai
        db = new CongViecDatabase(this);
        ReceivedCV = db.getCongViec(ReceiverID);

        //nhan cac gia tri
        mTieuDe = ReceivedCV.getTencv();
        mMoTa = ReceivedCV.getMota();
        mNgay = ReceivedCV.getNgay();
        mGio = ReceivedCV.getGio();
        mKieuLapLai = ReceivedCV.getKieulaplai();
        mTrangThai = ReceivedCV.getTrangthai();

        //
        TieuDeText.setText(mTieuDe);
        MoTaText.setText(mMoTa);
        NgayText.setText(mNgay);
        GioText.setText(mGio);
        LapText.setText(mKieuLapLai);

        // Để lưu trạng thái xoay vòng thiết bị
        if (savedInstanceState != null) {
            String savedTieuDe = savedInstanceState.getString(KEY_TENCV);
            TieuDeText.setText(savedTieuDe);
            mTieuDe = savedTieuDe;

            String savedMoTa = savedInstanceState.getString(KEY_MOTA);
            MoTaText.setText(savedMoTa);
            mMoTa = savedMoTa;

            String savedTime = savedInstanceState.getString(KEY_GIO);
            GioText.setText(savedTime);
            mGio= savedTime;

            String savedDate = savedInstanceState.getString(KEY_NGAY);
            NgayText.setText(savedDate);
            mNgay = savedDate;

            String saveLap = savedInstanceState.getString(KEY_KIEULAPLAI);
            LapText.setText(saveLap);
            mKieuLapLai = saveLap;

            mTrangThai = savedInstanceState.getString(KEY_TRANGTHAI);
        }

        // Lấy thông tin Ngày và Giờ
        calendar = Calendar.getInstance();
        alarmReceiver = new AlarmReceiver();

        mDateSplit = mNgay.split("/");
        mTimeSplit = mGio.split(":");

        mDay = Integer.parseInt(mDateSplit[0]);
        mMonth = Integer.parseInt(mDateSplit[1]);
        mYear = Integer.parseInt(mDateSplit[2]);
        mHour = Integer.parseInt(mTimeSplit[0]);
        mMinute = Integer.parseInt(mTimeSplit[1]);

        btnChonGio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chongio();
            }
        });

        btnChonNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chonngay();
            }
        });

        btnLap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chonkieulap();
            }
        });

    }
    //
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(KEY_TENCV, TieuDeText.getText());

        outState.putCharSequence(KEY_GIO, GioText.getText());
        outState.putCharSequence(KEY_NGAY, NgayText.getText());
        outState.putCharSequence(KEY_KIEULAPLAI, LapText.getText());
        outState.putCharSequence(KEY_TRANGTHAI, mTrangThai);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    // Khi nhấp vào Trình chọn giờ
    private  void chongio()
    {
        calendar=Calendar.getInstance();
        int phut = calendar.get(Calendar.MINUTE);
        int gio = calendar.get(Calendar.HOUR_OF_DAY);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                SimpleDateFormat simpledateFormat = new SimpleDateFormat("HH:mm");
                calendar.set(calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(calendar.MINUTE,minute);
                mHour = hourOfDay;
                mMinute = minute;
                GioText.setText(simpledateFormat.format(calendar.getTime()));
                mGio = hourOfDay + ":" +minute;
            }
        },gio, phut, true);
        timePickerDialog.show();
    }
    //
    private void chonngay()
    {
        calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat simpledateFormat = new SimpleDateFormat("dd/MM/yyyy");
                calendar.set(calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(calendar.MONTH,month);
                calendar.set(calendar.YEAR,year);
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
                NgayText.setText(simpledateFormat.format(calendar.getTime()));
                mNgay = dayOfMonth + "/" + month +"/" + year;
            }
        }, nam, thang , ngay);
        datePickerDialog.show();
    }

    // chon lai kieu lap
    public void chonkieulap(){
        final String[] items = new String[5];

        items[0] = "No Repeat";
        items[1] = "Hour";
        items[2] = "Day";
        items[3] = "Week";
        items[4] = "Month";

        // Tạo hộp thoại danh sách
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Type");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                mKieuLapLai = items[item];
                LapText.setText(mKieuLapLai);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /////
    // Khi nhấp vào nút cập nhật
    public void updateCV(){
        // Đặt giá trị mới trong lời nhắc
        ReceivedCV.setTencv(mTieuDe);
        ReceivedCV.setMota(mMoTa);
        ReceivedCV.setNgay(mNgay);
        ReceivedCV.setGio(mGio);
        ReceivedCV.setKieulaplai(mKieuLapLai);
        ReceivedCV.setTrangthai(mTrangThai);

        // Update CV
        db.updateCongViec(ReceivedCV);

        // Thiết lập lịch để tạo thông báo
        calendar.set(Calendar.MONTH, mMonth);
        calendar.set(Calendar.YEAR, mYear);
        calendar.set(Calendar.DAY_OF_MONTH, mDay);
        calendar.set(Calendar.HOUR_OF_DAY, mHour);
        calendar.set(Calendar.MINUTE, mMinute);
        calendar.set(Calendar.SECOND, 0);

        // Hủy thông báo hiện có của lời nhắc bằng cách sử dụng ID của nó
        alarmReceiver.cancelAlarm(getApplicationContext(), ReceiverID);

        // Kiểm tra loại lặp lại
        if (mKieuLapLai.equals("No Repeat")) {
            Repeat = 0;
        } else if (mKieuLapLai.equals("Hour")) {
            Repeat = milHour;
        } else if (mKieuLapLai.equals("Day")) {
            Repeat = milDay;
        } else if (mKieuLapLai.equals("Week")) {
            Repeat = milWeek;
        } else if (mKieuLapLai.equals("Month")) {
            Repeat = milMonth;
        }

        // Tạo một thông báo mới
        if (mTrangThai.equals("true")) {
            //
            if (mKieuLapLai.equals("Hour")||mKieuLapLai.equals("Day")||mKieuLapLai.equals("Week")||mKieuLapLai.equals("Month")) {
                alarmReceiver.setRepeatAlarm(getApplicationContext(), calendar, ReceiverID, Repeat);
            } else if (mKieuLapLai.equals("No Repeat")) {
                alarmReceiver.setAlarm(getApplicationContext(), calendar, ReceiverID);
            }
        }

        Toast.makeText(getApplicationContext(), "Đã Sửa",
                Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    // Nhấn nút quay lại
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // Tạo menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // su kien menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case save:
                TieuDeText.setText(mTieuDe);

                if (TieuDeText.getText().toString().length() == 0)
                    TieuDeText.setError("Tên Công Việc Không Được Để Trống!!!");

                else {
                    updateCV();
                }
                return true;

            case R.id.discard:
                Toast.makeText(getApplicationContext(), "Thay Đổi Bị Hủy!!!",
                        Toast.LENGTH_SHORT).show();

                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
