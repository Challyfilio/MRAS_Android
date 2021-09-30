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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.sql.*;

public class RegisActivity extends AppCompatActivity {

    private Button register_go;
    private EditText newaccountEdit;
    private EditText newpasswordEdit;
    private EditText newpasswordagainEdit;
    private RadioGroup identity;
    private RadioButton doctor;
    private RadioButton patient;
    String login_identity = "doctor";
    String sql1, sql2;
    String newaccount;
    Connection conn;
    Statement stat;
    ResultSet rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);

        register_go = (Button) findViewById(R.id.register_go);
        newaccountEdit = (EditText) findViewById(R.id.new_edit_name);
        newpasswordEdit = (EditText) findViewById(R.id.new_edit_password);
        newpasswordagainEdit = (EditText) findViewById(R.id.new_edit_password_again);
        identity = (RadioGroup) findViewById(R.id.identity);
        doctor = (RadioButton) findViewById(R.id.rbt_doctor);
        patient = (RadioButton) findViewById(R.id.rbt_patient);

        identity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (doctor.getId() == i) {
                    login_identity = "doctor";
                    //Toast.makeText(RegisActivity.this, login_identity, Toast.LENGTH_SHORT).show();
                }
                if (patient.getId() == i) {
                    login_identity = "patient";
                    //Toast.makeText(RegisActivity.this, login_identity, Toast.LENGTH_SHORT).show();
                }
            }
        });

        register_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sql1 = "select * from user where identity = '" + login_identity + "'";
                sql2 = "insert into user(account,passwd,identity) values('" + newaccountEdit.getText().toString() + "','" + newpasswordEdit.getText().toString() + "','" + login_identity + "')";
                new Thread() {
                    public void run() {
                        Looper.prepare();
                        boolean flag_same = false;
                        String newpassword = newpasswordEdit.getText().toString();
                        String newpasswordagain = newpasswordagainEdit.getText().toString();
                        conn = DatabaseHelper.openConnection();
                        rs = DatabaseHelper.getResult(conn, sql1);
                        if ((newaccountEdit.getText().toString().isEmpty()) || (newpasswordEdit.getText().toString().isEmpty()) || (newpasswordagainEdit.getText().toString().isEmpty())) {
                            Toast.makeText(RegisActivity.this, "不能为空,请重新输入", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                while (rs.next()) {
                                    newaccount = rs.getString("account");
                                    if ((newaccountEdit.getText().toString().equals(newaccount))) {
                                        flag_same = true;
                                        Toast.makeText(RegisActivity.this, "已存在此用户,请重新注册", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                if (!flag_same) {
                                    if (!newpassword.equals(newpasswordagain)) {
                                        Toast.makeText(RegisActivity.this, "密码不一致,请重新输入", Toast.LENGTH_SHORT).show();
                                    } else {
                                        DatabaseHelper.exeStat(conn, sql2);
                                        Toast.makeText(RegisActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
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