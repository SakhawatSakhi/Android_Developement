package com.SakhiCoder.attendanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    ClassAdapter classAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ClassItem> classItems = new ArrayList<>();
    Toolbar toolbar;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);

        fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(view -> showDialog());

        loadData();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        classAdapter = new ClassAdapter(this, classItems);
        recyclerView.setAdapter(classAdapter);
        classAdapter.setOnItemClickListener(position -> gotoItemActivity(position));

        setToolbar();
    }

    public void loadData() {
        Cursor cursor = dbHelper.getClassTable();
        classItems.clear();

        int idColumnIndex = cursor.getColumnIndex(DbHelper.C_ID);
        int classNameColumnIndex = cursor.getColumnIndex(DbHelper.CLASS_NAME_KEY);
        int subjectNameColumnIndex = cursor.getColumnIndex(DbHelper.SUBJECT_NAME_KEY);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(idColumnIndex);
            if (idColumnIndex >= 0) {
                String className = cursor.getString(classNameColumnIndex);
                if (classNameColumnIndex >= 0) {
                    String subjectName = cursor.getString(subjectNameColumnIndex);
                    if (subjectNameColumnIndex >= 0) {
                        classItems.add(new ClassItem(id, className, subjectName));
                    } else {
                        // Handle the case where SUBJECT_NAME_KEY column doesn't exist
                        Toast.makeText(this, "SUBJECT_NAME_KEY", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle the case where CLASS_NAME_KEY column doesn't exist
                    Toast.makeText(this, " CLASS_NAME_KEY", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Handle the case where C_ID column doesn't exist
                Toast.makeText(this, " C_ID", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);
        back.setOnClickListener(v -> Logout());

        title.setText("Class List");
        subtitle.setVisibility(View.GONE);
//        back.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
    }

    private void Logout() {
        startActivity(new Intent(this, LoginActivity.class));
        Toast.makeText(this, "Logout Successfully!", Toast.LENGTH_SHORT).show();
    }

    private void gotoItemActivity(int position) {
        Intent intent = new Intent(this, StudentActivity.class);
        intent.putExtra("className", classItems.get(position).getClassName());
        intent.putExtra("subjectName", classItems.get(position).getSubjectName());
        intent.putExtra("position", position);
        intent.putExtra("cid", classItems.get(position).getCid());
        startActivity(intent);
    }

    private void showDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(), MyDialog.CLASS_ADD_DIALOG);
        dialog.setListener((className, subjectName) -> addClass(className, subjectName));
    }

    //Start Add Class
    private void addClass(String className, String subjectName) {
        long cid = dbHelper.addClass(className, subjectName);
        ClassItem classItem = new ClassItem(cid, className, subjectName);
        classItems.add(classItem);
        classAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Added Class!", Toast.LENGTH_SHORT).show();
    }//End Start Add Class

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                showUpdateDialog(item.getGroupId());
                break;
            case 1:
                deleteClass(item.getGroupId());
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(int position) {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(), MyDialog.CLASS_UPDATE_DIALOG);
        dialog.setListener((className, subjectName) -> updateClass(position, className, subjectName));
    }

    //Start Update Class
    private void updateClass(int position, String className, String subjectName) {
        dbHelper.updateClass(classItems.get(position).getCid(), className, subjectName);
        classItems.get(position).setClassName(className);
        classItems.get(position).setSubjectName(subjectName);
        classAdapter.notifyItemChanged(position);
        Toast.makeText(this, "Update Successfully!", Toast.LENGTH_SHORT).show();
    }//End Start Update Class

    //Start Delete Class
    public void deleteClass(int position) {
        dbHelper.deleteClass(classItems.get(position).getCid());
        classItems.remove(position);
        classAdapter.notifyItemRemoved(position);
        Toast.makeText(this, "Delete Successfully!", Toast.LENGTH_SHORT).show();
    }//End Delete Class
}