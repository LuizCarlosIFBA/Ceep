package com.rashid.sqliteexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Reshka on 22.03.2016.
 */
public class ShowDetails extends Activity {

    EditText etNomeDeatil, etNotaDetail;
    Button btnDelete, btnUpdate;
    DbCon dbCon = new DbCon(this);
    String id,nome,nota;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showdetails);
        etNomeDeatil = (EditText) findViewById(R.id.etNameDetail);
        etNotaDetail = (EditText) findViewById(R.id.etSurnameDetail);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        Intent intent = getIntent();
        nome = intent.getStringExtra("sendNome");
        nota = intent.getStringExtra("sendNota");
        id = intent.getStringExtra("sendId");
        etNomeDeatil.setText(nome);
        etNotaDetail.setText(nota);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbCon.delete(Long.parseLong(id));
                returnHome();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbCon.update(Long.parseLong(id),etNomeDeatil.getText().toString(),etNotaDetail.getText().toString());
                returnHome();
            }
        });

    }

    public void returnHome(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}
