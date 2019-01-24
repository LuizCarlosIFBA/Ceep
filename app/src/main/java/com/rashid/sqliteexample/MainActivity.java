package com.rashid.sqliteexample;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    EditText name, surname;
    Button btnAdd;
    ListView lst;
    DbCon dbcon;
    DbCon.DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = (EditText) findViewById(R.id.etNome);
        surname = (EditText) findViewById(R.id.etNota);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        dbcon=new DbCon(this);
        load();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dbcon.insert(name.getText().toString(), surname.getText().toString());
                    load();
                } catch (Exception e) {

                }
            }
        });

            lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Cursor c=dbcon.selected(id);
                    Intent intent = new Intent(MainActivity.this,ShowDetails.class);
                    String sendName=c.getString(1);
                    String sendSurname=c.getString(2);
                    String sendId=c.getString(0);
                    intent.putExtra("sendName",sendName);
                    intent.putExtra("sendSurname",sendSurname);
                    intent.putExtra("sendId",sendId);
                    startActivity(intent);
                }
            });
    }


    public void load() {
        Cursor cursor = null;
        try {
            dbcon.open();
            cursor = dbcon.readAll();
        } catch (Exception e) {

        }
        String[] from = new String[]{dbHelper.ID, dbHelper.NOME, dbHelper.NOTA};
        int[] to = new int[]{R.id.tvId, R.id.tvName, R.id.tvSurname};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(MainActivity.this, R.layout.showlist, cursor, from, to);
        adapter.notifyDataSetChanged();
        lst = (ListView) findViewById(R.id.lst);
        lst.setAdapter(adapter);
    }
}
