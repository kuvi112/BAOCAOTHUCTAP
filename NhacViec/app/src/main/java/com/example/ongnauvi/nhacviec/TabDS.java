package com.example.ongnauvi.nhacviec;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

public class TabDS extends AppCompatActivity {

    private RecyclerView RLV;
    private SimpleAdapter CVAdapter;
    private CongViecDatabase congViecDatabase;
    private int TempPost;
    private LinkedHashMap<Integer, Integer> IDmap = new LinkedHashMap<>();
    private MultiSelector mMultiSelector = new MultiSelector();
    private AlarmReceiver alarmReceiver;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_ds);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TabDS.this,ThemCV.class));
            }
        });



        //khoi tao csdl
        congViecDatabase = new CongViecDatabase(getApplicationContext());

        //anh xa
        RLV = (RecyclerView) findViewById(R.id.CV_list);
        toolbar = (Toolbar)findViewById(R.id.toolbarDS);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Danh Sách Công Việc");

        // tao che do xem
        RLV.setLayoutManager(getLayoutManager());
        registerForContextMenu(RLV);
        CVAdapter = new SimpleAdapter();
        CVAdapter.setItemCount(getDefaultItemCount());
        RLV.setAdapter(CVAdapter);

        //khoi tao CV
        alarmReceiver = new AlarmReceiver();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu, menu);
    }

    // chon nhieu muc trong DS
    private android.support.v7.view.ActionMode.Callback mDeleteMode = new ModalMultiSelectorCallback(mMultiSelector) {

        @Override
        public boolean onCreateActionMode(android.support.v7.view.ActionMode actionMode, Menu menu) {
            getMenuInflater().inflate(R.menu.menu, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(android.support.v7.view.ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                //
                case R.id.discard:
                    //
                    actionMode.finish();
                    //
                    for (int i = IDmap.size(); i >= 0; i--) {
                        if (mMultiSelector.isSelected(i, 0)) {
                            int id = IDmap.get(i);
                            //
                            ClassCV temp = congViecDatabase.getCongViec(id);
                            //
                            congViecDatabase.deleteCongViec(temp);
                            //
                            CVAdapter.removeItemSelected(i);
                            //
                            alarmReceiver.cancelAlarm(getApplicationContext(), id);
                        }
                    }
                    //
                    mMultiSelector.clearSelections();
                    //
                    CVAdapter.onDeleteItem(getDefaultItemCount());
                    //
                    Toast.makeText(getApplicationContext(), "Xóa Thành Công!!!", Toast.LENGTH_SHORT).show();

                    return true;

                //
                case R.id.save:
                    //
                    actionMode.finish();
                    //
                    mMultiSelector.clearSelections();
                    return true;

                default:
                    break;
            }
            return false;
        }
    };

    // click chuot vao 1 CV
    private void selectCV(int mClickID) {
        String mStringClickID = Integer.toString(mClickID);

        Intent i = new Intent(this, ChinhSuaCV.class);
        i.putExtra(ChinhSuaCV.KEY_ID, mStringClickID);
        startActivityForResult(i, 1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        CVAdapter.setItemCount(getDefaultItemCount());
    }

    //hien thi Cv moi
    @Override
    public void onResume(){
        super.onResume();
        CVAdapter.setItemCount(getDefaultItemCount());
    }

    //quan ly bo cuc
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }
    protected int getDefaultItemCount() {
        return 100;
    }

    //adapter
    //
    public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.VerticalItemHolder> {
        private ArrayList<CVItem> mItems;

        public SimpleAdapter() {
            mItems = new ArrayList<>();
        }

        public void setItemCount(int count) {
            mItems.clear();
            mItems.addAll(generateData(count));
            notifyDataSetChanged();
        }

        public void onDeleteItem(int count) {
            mItems.clear();
            mItems.addAll(generateData(count));
        }

        public void removeItemSelected(int selected) {
            if (mItems.isEmpty()) return;
            mItems.remove(selected);
            notifyItemRemoved(selected);
        }

        //
        @Override
        public VerticalItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            View root = inflater.inflate(R.layout.chitiet_cv, container, false);

            return new VerticalItemHolder(root, this);
        }

        @Override
        public void onBindViewHolder(VerticalItemHolder itemHolder, int position) {
            CVItem item = mItems.get(position);
            itemHolder.setCVTitle(item.mTieuDe);
            itemHolder.setCVMoTa(item.mMoTa);
            itemHolder.setCVDateTime(item.mDateTime);
            itemHolder.setLapCV(item.mLap);
            itemHolder.setTrangThaiImage(item.mTrangThai);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        //
        public  class CVItem {
            public String mTieuDe;
            public String mMoTa;
            public String mDateTime;
            public String mLap;
            public String mTrangThai;

            public CVItem(String TieuDe,String MoTa, String DateTime, String Lap, String TrangThai) {
                this.mTieuDe = TieuDe;
                this.mMoTa = MoTa;
                this.mDateTime = DateTime;
                this.mLap = Lap;
                this.mTrangThai = TrangThai;
            }
        }

        //
        public class DateTimeComparator implements Comparator {
            DateFormat f = new SimpleDateFormat("dd/mm/yyyy hh:mm");

            public int compare(Object a, Object b) {
                String o1 = ((DateTimeSorter)a).getDateTime();
                String o2 = ((DateTimeSorter)b).getDateTime();

                try {
                    return f.parse(o1).compareTo(f.parse(o2));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }

        //
        public  class VerticalItemHolder extends SwappingHolder
                implements View.OnClickListener, View.OnLongClickListener {
            private TextView mTieuDe, mMoTaText, mDateTimeText, mLapText;
            private ImageView mTrangThaiImage , mAnhImage;
            private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
            private TextDrawable mDrawableBuilder;
            private SimpleAdapter mAdapter;

            public VerticalItemHolder(View itemView, SimpleAdapter adapter) {
                super(itemView, mMultiSelector);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
                itemView.setLongClickable(true);

                //
                mAdapter = adapter;

                //
                mTieuDe = (TextView) itemView.findViewById(R.id.txtTitle);
                mDateTimeText = (TextView) itemView.findViewById(R.id.txtTime);
                mMoTaText = (TextView) itemView.findViewById(R.id.txtMoTa);
                mLapText = (TextView) itemView.findViewById(R.id.txtLap);
                mTrangThaiImage = (ImageView) itemView.findViewById(R.id.ImageTT);
                mAnhImage = (ImageView) itemView.findViewById(R.id.ImageL);
            }

            // Khi nhấp vào một CV
            @Override
            public void onClick(View v) {
                if (!mMultiSelector.tapSelection(this)) {
                    TempPost = RLV.getChildAdapterPosition(v);

                    int mReminderClickID = IDmap.get(TempPost);
                    selectCV(mReminderClickID);

                } else if(mMultiSelector.getSelectedPositions().isEmpty()){
                    mAdapter.setItemCount(getDefaultItemCount());
                }
            }

            //
            @Override
            public boolean onLongClick(View v) {
                AppCompatActivity activity = TabDS.this;
                activity.startSupportActionMode(mDeleteMode);
                mMultiSelector.setSelected(this, true);
                return true;
            }

            //
            public void setCVTitle(String title) {
                mTieuDe.setText(title);
                String letter = "A";

                if(title != null && !title.isEmpty()) {
                    letter = title.substring(0, 1);
                }

                int color = mColorGenerator.getRandomColor();

                mDrawableBuilder = TextDrawable.builder()
                        .buildRound(letter, color);
                mAnhImage.setImageDrawable(mDrawableBuilder);
            }

            //
            public void setCVDateTime(String datetime) {
                mDateTimeText.setText(datetime);
            }

            public void setCVMoTa(String datetime) {
                mMoTaText.setText(datetime);
            }

            //
            public void setLapCV(String repeat) {
                if(repeat.equals("No Repeat")){
                    mLapText.setText("No Repeat");
                }else if (repeat.equals("Hour")) {
                    mLapText.setText("Hour");
                }else if (repeat.equals("Day")) {
                    mLapText.setText("Day");
                }else if (repeat.equals("Week")) {
                    mLapText.setText("Week");
                }else if (repeat.equals("Month")) {
                    mLapText.setText("Month");
                }
            }

            // chinh sua hinh chuong
            public void setTrangThaiImage(String active){
                if(active.equals("true")){
                    mTrangThaiImage.setImageResource(R.drawable.ic_notifications_on_white_24dp);
                }else if (active.equals("false")) {
                    mTrangThaiImage.setImageResource(R.drawable.ic_notifications_off_grey600_24dp);
                }
            }
        }

        //
        public List<CVItem> generateData(int count) {
            ArrayList<SimpleAdapter.CVItem> items = new ArrayList<>();

            // nhan tat ca CV tu csdl
            List<ClassCV> CV = congViecDatabase.getAllCongViec();

            // khoi tao DS
            List<String> TieuDe = new ArrayList<>();
            List<String> MoTa = new ArrayList<>();
            List<String> Lap = new ArrayList<>();
            List<String> TrangThai = new ArrayList<>();
            List<String> DateTime = new ArrayList<>();
            List<Integer> IDList= new ArrayList<>();
            List<DateTimeSorter> DateTimeSortList = new ArrayList<>();

            //
            for (ClassCV r : CV) {
                TieuDe.add(r.getTencv());
                MoTa.add(r.getMota());
                DateTime.add(r.getNgay() + " " + r.getGio());
                Lap.add(r.getKieulaplai());
                TrangThai.add(r.getTrangthai());
                IDList.add(r.getMacv());
            }

            int key = 0;

            //
            for(int k = 0; k<TieuDe.size(); k++){
                DateTimeSortList.add(new DateTimeSorter(key, DateTime.get(k)));
                key++;
            }

            // SX theo thu tu tang dan
            Collections.sort(DateTimeSortList, new DateTimeComparator());

            int k = 0;

            //
            for (DateTimeSorter item:DateTimeSortList) {
                int i = item.getIndex();

                items.add(new SimpleAdapter.CVItem(TieuDe.get(i),MoTa.get(i), DateTime.get(i), Lap.get(i),
                        TrangThai.get(i)));
                IDmap.put(k, IDList.get(i));
                k++;
            }
            return items;
        }
    }

}
