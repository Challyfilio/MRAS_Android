package com.example.david.mras;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.sql.*;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText accountEdit;
    private EditText passwordEdit;
    private Button login;
    private Button regis;
    private CheckBox rememberPass;
    private RadioGroup identity;
    private RadioButton doctor;
    private RadioButton patient;
    String account;
    String password;
    String sql1, sql2;
    String login_identity = "doctor";
    Connection conn;
    Statement stat;
    ResultSet rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        accountEdit = (EditText) findViewById(R.id.edit_name);
        passwordEdit = (EditText) findViewById(R.id.edit_password);
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);
        login = (Button) findViewById(R.id.bt_login);
        regis = (Button) findViewById(R.id.bt_register);
        boolean isRemember = pref.getBoolean("remember_password", false);
        identity = (RadioGroup) findViewById(R.id.identity);
        doctor = (RadioButton) findViewById(R.id.rbt_doctor);
        patient = (RadioButton) findViewById(R.id.rbt_patient);

        if (isRemember) {
            //将账号密码设置到文本框
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }

        //RadioButton监听器 2018.10.22 0950
        identity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (doctor.getId() == i) {
                    login_identity = "doctor";
                    //Toast.makeText(LoginActivity.this, login_identity, Toast.LENGTH_SHORT).show();
                }
                if (patient.getId() == i) {
                    login_identity = "patient";
                    //Toast.makeText(LoginActivity.this, login_identity, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //2018.10.22 1726
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sql1 = "select * from user where identity = '" + login_identity + "'";
                sql2 = "update tempuser set tempaccount = '" + accountEdit.getText().toString() + "',tempidentity = '" + login_identity + "'";
                new Thread() {
                    public void run() {
                        Looper.prepare();
                        boolean flag_log = false;
                        conn = DatabaseHelper.openConnection();
                        rs = DatabaseHelper.getResult(conn, sql1);
                        if ((accountEdit.getText().toString().isEmpty()) || (passwordEdit.getText().toString().isEmpty())) {
                            Toast.makeText(LoginActivity.this, "不能为空,请重新输入", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                while (rs.next()) {
                                    account = rs.getString("account");
                                    password = rs.getString("passwd");
                                    if ((accountEdit.getText().toString().equals(account)) && (passwordEdit.getText().toString().equals(password))) {
                                        editor = pref.edit();
                                        if (rememberPass.isChecked()) {
                                            editor.putBoolean("remember_password", true);
                                            editor.putString("account", account);
                                            editor.putString("password", password);
                                        } else {
                                            editor.clear();
                                        }
                                        editor.apply();
                                        flag_log = true;
                                        //DatabaseHelper.exeStat(conn,sql2);
                                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, ListActivity.class);
                                        intent.putExtra("tempaccount", accountEdit.getText().toString());
                                        intent.putExtra("tempidentity", login_identity);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
                            if (!flag_log) {
                                Toast.makeText(LoginActivity.this, "账号或密码错误,请重新输入", Toast.LENGTH_SHORT).show();
                            }
                        }
                        Looper.loop();
                    }
                }.start();
            }
        });

        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisActivity.class);
                startActivity(intent);
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

