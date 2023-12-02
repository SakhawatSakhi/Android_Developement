package com.SakhiCoder.attendanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {

    Toolbar toolbar;
    private String className;
    private String subjectName;
    private int position;
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<StudentItem> studentItems = new ArrayList<>();
    private MyCalendar calendar;
    private TextView subtitle;
    private DbHelper dbHelper;
    private int cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        dbHelper = new DbHelper(this);

        calendar = new MyCalendar();
        Intent intent = getIntent();
        className = intent.getStringExtra("className");
        subjectName = intent.getStringExtra("subjectName");
        position = intent.getIntExtra("position", -1);
        cid = intent.getIntExtra("cid", -1);


        setToolbar();
        loadData();
        recyclerView = findViewById(R.id.student_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StudentAdapter(this, studentItems);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> changeStatus(position));
        loadStatusData();
    }

    private void loadData() {
        Cursor cursor = dbHelper.getStudentTable(cid);
        studentItems.clear();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idColumnIndex = cursor.getColumnIndex(DbHelper.S_ID);
                int rollColumnIndex = cursor.getColumnIndex(DbHelper.STUDENT_ROLL_KEY);
                int nameColumnIndex = cursor.getColumnIndex(DbHelper.STUDENT_NAME_KEY);
                if (idColumnIndex >= 0 && rollColumnIndex >= 0 && nameColumnIndex >= 0) {
                    long sid = cursor.getLong(idColumnIndex);
                    int roll = cursor.getInt(rollColumnIndex);
                    String name = cursor.getString(nameColumnIndex);
                    studentItems.add(new StudentItem(sid, roll, name));
                } else {
                    Toast.makeText(this, "You Can not get Student Data", Toast.LENGTH_SHORT).show();
                }
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    private void changeStatus(int position) {
        String status = studentItems.get(position).getStatus();

        if (status.equals("P")) status = "A";
        else status = "P";

        studentItems.get(position).setStatus(status);
        adapter.notifyItemChanged(position);
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);
        save.setOnClickListener(v -> saveStatus());

        title.setText(className);
        subtitle.setText(subjectName + " | " + calendar.getDate());

        back.setOnClickListener(v -> onBackPressed());
        toolbar.inflateMenu(R.menu.student_menu);
        toolbar.setOnMenuItemClickListener(menuItem -> onMenuItemClick(menuItem));
    }

        private void saveStatus() {
        for (StudentItem studentItem : studentItems){
            String status = studentItem.getStatus();
            if (status!="P") status = "A";
           long value= dbHelper.addStatus(studentItem.getSid(),cid,calendar.getDate(),status);

           if (value==-1)dbHelper.updateStatus(studentItem.getSid(),calendar.getDate(),status);
        }
            Toast.makeText(this, R.string.data_saved_message, Toast.LENGTH_SHORT).show();
    }
    private void loadStatusData(){
        for (StudentItem studentItem : studentItems){
            String status = String.valueOf(dbHelper.getStatus(studentItem.getSid(), calendar.getDate()));
            if (status!=null) studentItem.setStatus(status);
            else studentItem.setStatus("");
        }
        adapter.notifyDataSetChanged();
    }
    //STATUS CODING START
//    private void saveStatus() {
//        for (StudentItem studentItem : studentItems) {
//            String status = studentItem.getStatus();
//            if (!"P".equals(status)) {
//                status = "A";
//            }
//            long value = dbHelper.addStatus(studentItem.getSid(), cid, calendar.getDate(), status);
//
//            if (value == -1) {
//                dbHelper.updateStatus(studentItem.getSid(), calendar.getDate(), status);
//            }
//        }
//        Toast.makeText(this, R.string.data_saved_message, Toast.LENGTH_SHORT).show();
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    private void loadStatusData() {
//        for (StudentItem studentItem : studentItems) {
//            String status = dbHelper.getStatus(studentItem.getSid(), calendar.getDate());
//            if (status != null) {
//                studentItem.setStatus(status);
//            } else {
//                studentItem.setStatus(" ");
//            }
//        }
//        adapter.notifyDataSetChanged();
//    }//STATUS CODING END

    private boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.add_student) {
            showAddStudentDialog();
        } else if (menuItem.getItemId() == R.id.show_calendar) {
            showCalendar();
        } else if (menuItem.getItemId() == R.id.show_attendance_sheet) {
            openSheetList();//Sheet_List
        }
        return true;
    }
    private void openSheetList() {
        long[] idArray = new  long[studentItems.size()];
        int[] rollArray = new int[studentItems.size()];
        String[] nameArray = new String[studentItems.size()];
        for (int i =0 ; i<idArray.length ; i++)
            idArray[i] = studentItems.get(i).getSid();
        for (int i =0 ; i<rollArray.length ; i++)
            rollArray[i] =studentItems.get(i).getRoll();
        for (int i =0 ; i<nameArray.length ; i++)
            nameArray[i] = studentItems.get(i).getName();

        Intent intent = new Intent(this, SheetListActivity.class);//GO_TO_SheetListActivity
        intent.putExtra("cid",cid);
        intent.putExtra("idArray",idArray);
        intent.putExtra("rollArray",rollArray);
        intent.putExtra("nameArray",nameArray);
        startActivity(intent);
    }
    private void showCalendar() {
        calendar.show(getSupportFragmentManager(), "");
        calendar.setOnCalendarOkClickListener(this::onCalendarOkClicked);
    }

    private void onCalendarOkClicked(int year, int month, int day) {

        calendar.setDate(year, month, day);
        subtitle.setText(subjectName + " | " + calendar.getDate());
        loadStatusData();
        Toast.makeText(this, "Date Changed!", Toast.LENGTH_SHORT).show();
    } //End Add Calender or Date

    private void showAddStudentDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(), MyDialog.STUDENT_ADD_DIALOG);
        dialog.setListener((roll, name) -> addStudent(roll, name));
    }

    private void addStudent(String roll_string, String name) {
        int roll = Integer.parseInt(roll_string);
        long sid = dbHelper.addStudent(cid, roll, name);
        StudentItem studentItem = new StudentItem(sid, roll, name);
        studentItems.add(studentItem);
        //studentItems.add(new StudentItem(roll, name));
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                showUpdateStudentDialog(item.getGroupId());
                break;
            case 1:
                deleteStudent(item.getGroupId());
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateStudentDialog(int position) {
        MyDialog dialog = new MyDialog(studentItems.get(position).getRoll(), studentItems.get(position).getName());
        dialog.show(getSupportFragmentManager(), MyDialog.STUDENT_UPDATE_DIALOG);
        dialog.setListener((roll_string, name) -> updateStudent(position, name));
    }

    private void updateStudent(int position, String name) {
        dbHelper.updateStudent(studentItems.get(position), name);
        studentItems.get(position).setName(name);
        adapter.notifyItemChanged(position);
        Toast.makeText(this, "Student Updated!", Toast.LENGTH_SHORT).show();
    }

    private void deleteStudent(int position) {
        dbHelper.deleteStudent(studentItems.get(position).getSid());
        studentItems.remove(position);
        adapter.notifyItemRemoved(position);
        Toast.makeText(this, "Deleted Student!", Toast.LENGTH_SHORT).show();
    }
}