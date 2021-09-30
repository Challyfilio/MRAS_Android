package com.example.david.mras;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.*;

public class ChangeActivity extends AppCompatActivity {
    private Button confirm;
    String Rno_show, account_show, age_show, sex_show, tel_show, date_show, department_show,
            doctor_show, diagnosis_show, detail_show, opinion_show, tempidentity, tempaccount;
    EditText accountEdit, ageEdit, sexEdit, telEdit, dateEdit, departmentEdit, doctorEdit,
            diagnosisEdit, detailEdit, opinionEdit;
    TextView RnoText;
    Connection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        RnoText = (TextView) findViewById(R.id.text_Rno_c);
        accountEdit = (EditText) findViewById(R.id.edit_account);
        ageEdit = (EditText) findViewById(R.id.edit_age);
        sexEdit = (EditText) findViewById(R.id.edit_sex);
        telEdit = (EditText) findViewById(R.id.edit_tel);
        dateEdit = (EditText) findViewById(R.id.edit_date);
        departmentEdit = (EditText) findViewById(R.id.edit_department);
        doctorEdit = (EditText) findViewById(R.id.edit_doctor);
        diagnosisEdit = (EditText) findViewById(R.id.edit_diagnosis);
        detailEdit = (EditText) findViewById(R.id.edit_detail);
        opinionEdit = (EditText) findViewById(R.id.edit_opinion);
        confirm = (Button) findViewById(R.id.bt_confirm);

        final Intent intent = getIntent();
        Rno_show = intent.getStringExtra("a");
        account_show = intent.getStringExtra("b");
        age_show = intent.getStringExtra("c");
        sex_show = intent.getStringExtra("d");
        tel_show = intent.getStringExtra("e");
        date_show = intent.getStringExtra("f");
        department_show = intent.getStringExtra("g");
        doctor_show = intent.getStringExtra("h");
        diagnosis_show = intent.getStringExtra("i");
        detail_show = intent.getStringExtra("j");
        opinion_show = intent.getStringExtra("k");
        /*人类之光，不写会崩*/
        tempidentity = intent.getStringExtra("l");
        tempaccount = intent.getStringExtra("m");

        RnoText.setText(Rno_show);
        accountEdit.setText(account_show);
        ageEdit.setText(age_show);
        sexEdit.setText(sex_show);
        telEdit.setText(tel_show);
        dateEdit.setText(date_show);
        departmentEdit.setText(department_show);
        doctorEdit.setText(doctor_show);
        diagnosisEdit.setText(diagnosis_show);
        detailEdit.setText(detail_show);
        opinionEdit.setText(opinion_show);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread() {
                    String sql = "update info set account='" + accountEdit.getText().toString() + "'," +
                            "age=" + ageEdit.getText().toString() + "," +
                            "sex='" + sexEdit.getText().toString() + "'," +
                            "tel='" + telEdit.getText().toString() + "'," +
                            "date='" + dateEdit.getText().toString() + "'," +
                            "department='" + departmentEdit.getText().toString() + "'," +
                            "doctor='" + doctorEdit.getText().toString() + "'," +
                            "diagnosis='" + diagnosisEdit.getText().toString() + "'," +
                            "detail='" + detailEdit.getText().toString() + "'," +
                            "opinion='" + opinionEdit.getText().toString() + "'" +
                            "where Rno='" + Rno_show + "'";

                    public void run() {
                        Looper.prepare();
                        conn = DatabaseHelper.openConnection();
                        try {
                            DatabaseHelper.exeStat(conn, sql);
                            Toast.makeText(ChangeActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ChangeActivity.this, ListActivity.class);
                            /*人类之光，不写会崩*/
                            intent.putExtra("tempaccount", tempaccount);
                            intent.putExtra("tempidentity", tempidentity);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } finally {
                            if (conn != null) {
                                try {
                                    conn.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        Looper.loop();
                    }
                }.start();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_1:
                Toast.makeText(this, "Challyfilio", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_2:
                finish();
                break;
            default:
        }
        return true;
    }
}
