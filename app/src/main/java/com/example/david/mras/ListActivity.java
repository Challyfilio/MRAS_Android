package com.example.david.mras;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private TextView showaccount;
    private TextView showidentity;
    String tempaccount;
    String tempidentity;
    String Rno_show, account_show, age_show, sex_show, tel_show, date_show, department_show,
            doctor_show, diagnosis_show, detail_show, opinion_show;
    String sql = "select * from info";
    ListView recordView;
    ArrayAdapter<String> adapter;
    List<String> RecordList = new ArrayList<String>();
    Connection conn;
    Statement stat;
    ResultSet rs, rs1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        showaccount = (TextView) findViewById(R.id.account_login);
        showidentity = (TextView) findViewById(R.id.identity_login);
        //设置 listview,读取数据
        recordView = (ListView) findViewById(R.id.record_view);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, RecordList);
        recordView.setAdapter(adapter);

        final Intent intent = getIntent();
        tempaccount = intent.getStringExtra("tempaccount");
        tempidentity = intent.getStringExtra("tempidentity");
        showaccount.setText(tempaccount);
        showidentity.setText(tempidentity);

        if (tempidentity.equals("patient")) {
            //Toast.makeText(ListActivity.this, "这里会执行吗 patient", Toast.LENGTH_SHORT).show();
            sql = "select * from info where account = '" + tempaccount + "'";
        }
        if (tempidentity.equals("doctor")) {
            //Toast.makeText(ListActivity.this, "这里会执行吗 doctor", Toast.LENGTH_SHORT).show();
            sql = "select * from info";
        }

        new Thread() {
            public void run() {
                Looper.prepare();
                conn = DatabaseHelper.openConnection();
                rs = DatabaseHelper.getResult(conn, sql);
                try {
                    //Toast.makeText(ListActivity.this, "这里会执行吗 try", Toast.LENGTH_SHORT).show();
                    while (rs.next()) {
                        //Toast.makeText(ListActivity.this, "这里会执行吗 while", Toast.LENGTH_SHORT).show();
                        String name = rs.getString("account");
                        Log.d("ListActivity", name);
                        String Rno = rs.getString("Rno");
                        Log.d("ListActivity", Rno);
                        String date = rs.getString("date");
                        Log.d("ListActivity", date);
                        RecordList.add(name + "\n" + Rno + "\n" + date);
                        //Toast.makeText(ListActivity.this, "这里会执行吗 add", Toast.LENGTH_SHORT).show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
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
                Looper.loop();
            }
        }.start();
        //Toast.makeText(ListActivity.this, "这里会执行吗 read", Toast.LENGTH_SHORT).show();

        recordView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new Thread() {
                    public void run() {
                        Looper.prepare();
                        conn = DatabaseHelper.openConnection();
                        rs = DatabaseHelper.getResult(conn, sql);
                        try {
                            //执行查询
                            for (int i = 0; i <= position; i++) {
                                rs.next();
                            }
                            String tempRno = rs.getString("Rno");
                            //new
                            /**/
                            rs1 = DatabaseHelper.getResult(conn, "select * from info where Rno = '" + tempRno + "'");
                            while (rs1.next()) {
                                Rno_show = rs1.getString("Rno");
                                account_show = rs1.getString("account");
                                age_show = rs1.getString("age");
                                sex_show = rs1.getString("sex");
                                tel_show = rs1.getString("tel");
                                date_show = rs1.getString("date");
                                department_show = rs1.getString("department");
                                doctor_show = rs1.getString("doctor");
                                diagnosis_show = rs1.getString("diagnosis");
                                detail_show = rs1.getString("detail");
                                opinion_show = rs1.getString("opinion");
                            }
                            Intent intent = new Intent(ListActivity.this, ShowActivity.class);
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
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            /**/
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            if (rs != null) {
                                try {
                                    rs.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (rs1 != null) {
                                try {
                                    rs1.close();
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
                        Looper.loop();
                    }
                }.start();
            }
        });

    }

    //private void readRecord() {

    //}

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_1:
                if (tempidentity.equals("patient")) {
                    Toast.makeText(this, "您无权添加病历", Toast.LENGTH_SHORT).show();
                }
                if (tempidentity.equals("doctor")) {
                    Intent intent = new Intent(ListActivity.this, InsertActivity.class);
                    intent.putExtra("l", tempidentity);
                    intent.putExtra("m", tempaccount);
                    startActivity(intent);
                }
                break;
            case R.id.item_2:
                finish();
                break;
            default:
        }
        return true;
    }
}