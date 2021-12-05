package com.example.diary.list;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


import com.example.diary.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

public class ListMainActivity extends AppCompatActivity {

    /*
        private ImageButton okBtn;
        private ImageButton notBtn;
        private TextView textView;*/
    private MyDataControl mydb;
    private List<Record> myList;
    private ListView lView;
    private CardAdapter cardAdapter;
    private FloatingActionButton floatingActionButton;
    /*
    private ImageButton hisBtn;
    private ImageButton delBtn;*/
    private Record record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_t2);

        lView = findViewById(R.id.card_list);
        mydb = MyDataControl.getAvailable(getApplicationContext());
        myList = mydb.getRecords();
        if (myList.size() == 0) {
            record = new Record();
            record.setTitle("ToDoList");
            mydb.addRecord(record);
            myList = mydb.getRecords();
        } else {
            record = myList.get(myList.size() - 1);
        }
        cardAdapter = new CardAdapter(ListMainActivity.this, R.layout.list_card, myList);
        lView.setAdapter(cardAdapter);

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(v -> {
            AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(ListMainActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);

            //alterDiaglog.setView(R.layout.input_dialog);//加载进去
            LayoutInflater inflater = getLayoutInflater();
            View vie = inflater.inflate(R.layout.list_input_dialog, null);
            EditText edt1 = vie.findViewById(R.id.edit_desc);
            EditText edt2 = vie.findViewById(R.id.edit_target);

            alterDiaglog.setView(vie);
            alterDiaglog.setPositiveButton(R.string.submit, null);
            alterDiaglog.setNegativeButton(R.string.cancel, null);
            AlertDialog dialog = alterDiaglog.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener((vi) -> {
                Record rec = new Record();
                rec.setDate(new Date());
                if(edt1.getText().toString().equals("") || edt2.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),R.string.dialog_blank,Toast.LENGTH_SHORT).show();
                    return;
                }
                String title = edt1.getText().toString();
                int target = Integer.parseInt(edt2.getText().toString());
                rec.setTitle(title);
                rec.setTarget(target);
                myList.add(rec);
                mydb.addRecord(rec);
                cardAdapter.notifyDataSetChanged();
                dialog.dismiss();
            });
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener((vi) -> {
                Toast.makeText(getApplicationContext(), R.string.canceled, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });

        });


    }

}
