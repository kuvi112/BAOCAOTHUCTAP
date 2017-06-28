package com.example.ongnauvi.nhacviec;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

//import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

public class ThemCV extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editTenCv, editMoTa;
    private TextView txtNgay, txtGio, txtLap;
    private Calendar calendar;
    private int mYear, mHour,mMinute,mMonth,mDay;
    private long RepeatTime;
    private String TrangThai,Lap,Date,Time;
    private String TieuDe,MoTa;
    private Button btnChonNgay, btnGio,btnLap;


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_cv);

        //database
        final CongViecDatabase DB =new CongViecDatabase(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        editTenCv= (EditText)findViewById(R.id.editTenCv);
        editMoTa=(EditText) findViewById(R.id.editMt);
        txtNgay =(TextView) findViewById(R.id.txtNgay);
        txtGio = (TextView) findViewById(R.id.txtGio);
        txtLap =(TextView) findViewById(R.id.txtKieuLap);
        btnChonNgay =(Button)findViewById(R.id.BtnNgay);
        btnGio =(Button) findViewById(R.id.BtnGio);
        btnLap =(Button)findViewById(R.id.BtnLap);

        // Cài đặt Thanh công cụ
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thêm Công Việc");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Khởi tạo các giá trị mặc định
        TrangThai = "true";
        Lap = "No Repeat";

        calendar = Calendar.getInstance();
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH)+ 1;
        mDay = calendar.get(Calendar.DATE);

        Date = mDay + "/" + mMonth + "/" + mYear;
        Time = mHour + ":" + mMinute;

        //
        editTenCv.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TieuDe = s.toString().trim();
                editTenCv.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        editMoTa.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MoTa = s.toString().trim();
                editMoTa.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        //
        txtNgay.setText(Date);
        txtGio.setText(Time);
        txtLap.setText(Lap);

        // Để lưu trạng thái xoay vòng thiết bị
        if (savedInstanceState != null) {
            String savedTieuDe = savedInstanceState.getString(KEY_TENCV);
            editTenCv.setText(savedTieuDe);
            TieuDe = savedTieuDe;

            String savedMoTa = savedInstanceState.getString(KEY_MOTA);
            editMoTa.setText(savedMoTa);
            MoTa = savedMoTa;

            String savedTime = savedInstanceState.getString(KEY_GIO);
            txtGio.setText(savedTime);
            Time = savedTime;

            String savedDate = savedInstanceState.getString(KEY_NGAY);
            txtNgay.setText(savedDate);
            Date = savedDate;

            String saveRepeat = savedInstanceState.getString(KEY_KIEULAPLAI);
            txtLap.setText(saveRepeat);
            Lap = saveRepeat;

            TrangThai = savedInstanceState.getString(KEY_TRANGTHAI);
        }

        btnLap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chonkieulap();
            }
        });
        btnGio.setOnClickListener(new View.OnClickListener() {
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
    }
    // Để lưu trạng thái xoay vòng thiết bị
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(KEY_TENCV, editTenCv.getText());
        outState.putCharSequence(KEY_MOTA, editMoTa.getText());
        outState.putCharSequence(KEY_GIO, txtGio.getText());
        outState.putCharSequence(KEY_NGAY, txtNgay.getText());
        outState.putCharSequence(KEY_KIEULAPLAI, txtLap.getText());
        outState.putCharSequence(KEY_TRANGTHAI, TrangThai);
    }

    // Khi nhấp vào Trình chọn giờ

    private void chonngay()
    {
        final Calendar ChonNgay;
        ChonNgay = Calendar.getInstance();
        int ngay = ChonNgay.get(Calendar.DATE);
        int thang = ChonNgay.get(Calendar.MONTH);
        int nam = ChonNgay.get(Calendar.YEAR);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat simpledateFormat = new SimpleDateFormat("dd/MM/yyyy");
                //ChonNgay.set(year, month, dayOfMonth);
                ChonNgay.set(Calendar.YEAR,year);
                ChonNgay.set(Calendar.MONTH,month);
                ChonNgay.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                mDay = dayOfMonth;
                mMonth = month;
                mYear = year;
                txtNgay.setText(simpledateFormat.format(ChonNgay.getTime()));

                Date = dayOfMonth + "/" + month + "/" + year;
            }
        }, nam, thang , ngay);
        datePickerDialog.show();
    }
    private  void chongio()
    {
        final Calendar ChonGio;
        ChonGio=Calendar.getInstance();
        int phut = ChonGio.get(Calendar.MINUTE);
        int gio = ChonGio.get(Calendar.HOUR_OF_DAY);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                SimpleDateFormat simpledateFormat = new SimpleDateFormat("HH:mm");
                //ChonGio.set(0,0,0,hourOfDay, minute);
                ChonGio.set(Calendar.HOUR_OF_DAY,hourOfDay);
                ChonGio.set(Calendar.MINUTE,minute);
                mMinute = minute;
                mHour = hourOfDay;
                txtGio.setText(simpledateFormat.format(ChonGio.getTime()));

                Time = hourOfDay + ":" +minute;
            }
        },gio, phut, true);
        timePickerDialog.show();
    }
    // chon lai kieu lap
    public void chonkieulap(){
        final String[] items = new String[5];

        items[0] = "No Repeat";
        items[1] = "Hour";
        items[2] = "Day";
        items[3] = "Week";
        items[4] = "Month";

        // click vao btnLap
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn Kiểu Lặp");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                Lap = items[item];
                txtLap.setText(Lap);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // Khi nhấp vào nút lưu
    public void saveCV(){
        CongViecDatabase db = new CongViecDatabase(this);

        // Tạo lời nhắc
        int ID = db.ThemCV(new ClassCV(TieuDe,MoTa, Date, Time, Lap, TrangThai));

        // Thiết lập lịch để tạo thông báo
        calendar.set(Calendar.MONTH, mMonth);
        calendar.set(Calendar.YEAR, mYear);
        calendar.set(Calendar.DAY_OF_MONTH, mDay);
        calendar.set(Calendar.HOUR_OF_DAY, mHour);
        calendar.set(Calendar.MINUTE, mMinute);
        calendar.set(Calendar.SECOND, 0);

        // Kiểm tra loại lặp lại
        if (Lap.equals("No Repeat")) {
            RepeatTime = 0;
        } else if (Lap.equals("Hour")) {
            RepeatTime = milHour;
        } else if (Lap.equals("Day")) {
            RepeatTime = milDay;
        } else if (Lap.equals("Week")) {
            RepeatTime = milWeek;
        } else if (Lap.equals("Month")) {
            RepeatTime = milMonth;
        }

        // Tạo một thông báo mới
        if (TrangThai.equals("true")) {
            if (Lap.equals("Hour")||Lap.equals("Day")||Lap.equals("Week")||Lap.equals("Month")) {
                new AlarmReceiver().setRepeatAlarm(getApplicationContext(), calendar, ID, RepeatTime);
            } else if (Lap.equals("No Repeat")) {
                new AlarmReceiver().setAlarm(getApplicationContext(), calendar, ID);
            }
        }

        //
        Toast.makeText(getApplicationContext(), "Đã Lưu",
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

    //
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.save:
                editTenCv.setText(TieuDe);

                if (editTenCv.getText().toString().length() == 0)
                    editTenCv.setError("Tên Công Việc Không Được Để Trống!!!");

                else {
                    saveCV();
                }
                return true;

            // Nhấp vào nút huỷ lời nhắc
            // Hủy bất kỳ thay đổi nào
            case R.id.discard:
                Toast.makeText(getApplicationContext(), "Discarded",
                        Toast.LENGTH_SHORT).show();

                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
