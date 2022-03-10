package com.example.test20211227;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MemberActivity extends Activity {
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    TextView textView6;
    TextView modify;
    TextView cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_activity);

        textView1 = (TextView) findViewById(R.id.memberName);
        textView2 = (TextView) findViewById(R.id.memberPhone);
        textView3 = (TextView) findViewById(R.id.memberBirth);
        textView4 = (TextView) findViewById(R.id.memberGender);
        textView5 = (TextView) findViewById(R.id.memberAge);
        textView6 = (TextView) findViewById(R.id.memberEmail);
        modify = (TextView) findViewById(R.id.memberModify);
        cancel = (TextView) findViewById(R.id.memberCancel);


        textView1.setText(MainActivity_login.member.getNAME());
        textView2.setText(MainActivity_login.member.getPhoneNumber());
        textView3.setText(MainActivity_login.member.getBirth());
        textView4.setText(MainActivity_login.member.getGender());
        textView5.setText(String.valueOf(MainActivity_login.member.getAge()));
        textView6.setText(MainActivity_login.member.getEmail());

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MemberModifyActivity.class);
                startActivity(intent);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }


}
