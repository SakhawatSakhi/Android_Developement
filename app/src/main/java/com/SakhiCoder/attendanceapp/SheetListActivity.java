package com.SakhiCoder.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class SheetListActivity extends AppCompatActivity {
    private ListView sheetList;
    private ArrayAdapter adapter;
    private ArrayList<String> listItems = new ArrayList<>();
    private long cid;
    Toolbar toolbar;
    private TextView subtitle;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_list);

        cid = getIntent().getLongExtra("cid",-1);
        loadListItems();
        sheetList = findViewById(R.id.sheetlist);
        adapter = new ArrayAdapter(this,R.layout.sheet_list,R.id.date_list_item,listItems);
        sheetList.setAdapter(adapter);

        sheetList.setOnItemClickListener((parent, view, position, id) -> openSheetActivity(position));
        setToolbar();

    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        back.setOnClickListener(v -> onBackPressed());
        title.setText("Sheet List");
        subtitle.setVisibility(View.GONE);
        save.setVisibility(View.INVISIBLE);
    }

    private void openSheetActivity(int position) {
        long[] idArray = getIntent().getLongArrayExtra("idArray");
        int[] rollArray = getIntent().getIntArrayExtra("rollArray");
        String[] nameArray =getIntent().getStringArrayExtra("nameArray");

        Intent intent =new Intent(this,SheetActivity.class);//SHOW_ATTENDANCE_SHEET
        intent.putExtra("idArray",idArray);
        intent.putExtra("rollArray",rollArray);
        intent.putExtra("nameArray",nameArray);
       intent.putExtra("month",listItems.get(position));

        startActivity(intent);
    }

    private void loadListItems() {
        DbHelper dbHelper = new DbHelper(this);
        Cursor cursor = dbHelper.getDistinctMonths(cid);
        if (cursor != null && cursor.moveToFirst()) {
            int dateColumnIndex = cursor.getColumnIndexOrThrow(DbHelper.DATE_KEY);

            do {
                String date = cursor.getString(dateColumnIndex);
                if (date != null && date.length() >= 3) {
                    listItems.add(date.substring(3));
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        dbHelper.close();
    }
//    private void loadListItems() {
//        Cursor cursor = new DbHelper(this).getDistinctMonths(cid);
//        while (cursor.moveToNext()) {
//             String date = cursor.getString(cursor.getColumnIndex(DbHelper.DATE_KEY));
//            listItems.add(date.substring(3));
//        }
//    }
}