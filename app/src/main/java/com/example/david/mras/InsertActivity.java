package com.example.david.mras;

import android.content.Intent;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.*;

public class InsertActivity extends AppCompatActivity {
    private Button insert;
    private EditText RnoIns, accountIns, ageIns, sexIns, telIns, dateIns, departmentIns,
            doctorIns, diagnosisIns, detailIns, opinionIns;
    String sql1, sql2, tempidentity, tempaccount, sameRno;
    Connection conn;
    Statement stat;
    ResultSet rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        RnoIns = (EditText) findViewById(R.id.ins_Rno);
        accountIns = (EditText) findViewById(R.id.ins_account);
        ageIns = (EditText) findViewById(R.id.ins_age);
        sexIns = (EditText) findViewById(R.id.ins_sex);
        telIns = (EditText) findViewById(R.id.ins_tel);
        dateIns = (EditText) findViewById(R.id.ins_date);
        departmentIns = (EditText) findViewById(R.id.ins_department);
        doctorIns = (EditText) findViewById(R.id.ins_doctor);
        diagnosisIns = (EditText) findViewById(R.id.ins_diagnosis);
        detailIns = (EditText) findViewById(R.id.ins_detail);
        opinionIns = (EditText) findViewById(R.id.ins_opinion);
        insert = (Button) findViewById(R.id.bt_insert);

        final Intent intent = getIntent();
        /*人类之光，不写会崩*/
        tempidentity = intent.getStringExtra("l");
        tempaccount = intent.getStringExtra("m");

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sql1 = "select * from info";
                sql2 = "insert into info(Rno,account,age,sex,tel,date,department,doctor,diagnosis,detail,opinion) values(" +
                        "'" + RnoIns.getText().toString() + "','" + accountIns.getText().toString() + "'," + ageIns.getText().toString() + "," +
                        "'" + sexIns.getText().toString() + "','" + telIns.getText().toString() + "','" + dateIns.getText().toString() + "'," +
                        "'" + departmentIns.getText().toString() + "','" + doctorIns.getText().toString() + "','" + diagnosisIns.getText().toString() + "'," +
                        "'" + detailIns.getText().toString() + "','" + opinionIns.getText().toString() + "')";
                new Thread() {
                    public void run() {
                        Looper.prepare();
                        boolean flag_same = false;
                        //String newRno = RnoIns.getText().toString();
                        conn = DatabaseHelper.openConnection();
                        rs = DatabaseHelper.getResult(conn, sql1);
                        if (RnoIns.getText().toString().isEmpty()) {
                            Toast.makeText(InsertActivity.this, "编号不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                while (rs.next()) {
                                    sameRno = rs.getString("Rno");
                                    if (RnoIns.getText().toString().equals(sameRno)) {
                                        flag_same = true;
                                        Toast.makeText(InsertActivity.this, "编号已存在,请重新输入", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                if (!flag_same) {
                                    DatabaseHelper.exeStat(conn, sql2);
                                    Toast.makeText(InsertActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(InsertActivity.this, ListActivity.class);
                                    intent.putExtra("tempaccount", tempaccount);
                                    intent.putExtra("tempidentity", tempidentity);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            } catch (SQLException e) {
                                e.getMessage();
                            } finally {
                                if (rs != null) {
                                    try {
                                        rs.close();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (stat != null) {
                                    try {
                                        stat.close();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (conn != null) {
                                    try {
                                        conn.close();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
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
