package com.example.david.mras;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.*;

public class ShowActivity extends AppCompatActivity {
    private Button delete;
    private Button change;
    String Rno_show, account_show, age_show, sex_show, tel_show, date_show, department_show,
            doctor_show, diagnosis_show, detail_show, opinion_show, tempidentity, tempaccount;
    Connection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        TextView RnoText = (TextView) findViewById(R.id.show_Rno);
        TextView accountText = (TextView) findViewById(R.id.show_account);
        TextView ageText = (TextView) findViewById(R.id.show_age);
        TextView sexText = (TextView) findViewById(R.id.show_sex);
        TextView telText = (TextView) findViewById(R.id.show_tel);
        TextView dateText = (TextView) findViewById(R.id.show_date);
        TextView departmentText = (TextView) findViewById(R.id.show_department);
        TextView doctorText = (TextView) findViewById(R.id.show_doctor);
        TextView diagnosisText = (TextView) findViewById(R.id.show_diagnosis);
        TextView detailText = (TextView) findViewById(R.id.show_detail);
        TextView opinionText = (TextView) findViewById(R.id.show_opinion);
        change = (Button) findViewById(R.id.bt_change);
        delete = (Button) findViewById(R.id.bt_delete);

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
        accountText.setText(account_show);
        ageText.setText(age_show);
        sexText.setText(sex_show);
        telText.setText(tel_show);
        dateText.setText(date_show);
        departmentText.setText(department_show);
        doctorText.setText(doctor_show);
        diagnosisText.setText(diagnosis_show);
        detailText.setText(detail_show);
        opinionText.setText(opinion_show);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tempidentity.equals("doctor")) {
                    Intent intent = new Intent(ShowActivity.this, ChangeActivity.class);
                    intent.putExtra("a", Rno_show);
                    intent.putExtra("b", account_show);
                    intent.putExtra("c", age_show);
                    intent.putExtra("d", sex_show);
                    intent.putExtra("e", tel_show);
                    intent.putExtra("f", date_show);
                    intent.putExtra("g", department_show);
                    intent.putExtra("h", doctor_show);
                    intent.putExtra("i", diagnosis_show);
                    intent.putExtra("j", detail_show);
                    intent.putExtra("k", opinion_show);
                    intent.putExtra("l", tempidentity);
                    intent.putExtra("m", tempaccount);
                    startActivity(intent);
                }
                if (tempidentity.equals("patient")) {
                    Toast.makeText(view.getContext(), "您无权修改病历", Toast.LENGTH_SHORT).show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tempidentity.equals("doctor")) {
                    delete();
                }
                if (tempidentity.equals("patient")) {
                    Toast.makeText(view.getContext(), "您无权删除病历", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void delete() {
        new Thread() {
            public void run() {
                Looper.prepare();
                conn = DatabaseHelper.openConnection();
                try {
                    DatabaseHelper.exeStat(conn, "delete from info where Rno='" + Rno_show + "'");
                    Toast.makeText(ShowActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ShowActivity.this, ListActivity.class);
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
