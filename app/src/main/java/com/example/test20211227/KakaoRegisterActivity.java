package com.example.test20211227;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class KakaoRegisterActivity extends Activity {
    EditText editText_phone;
    EditText editText_birth;
    EditText editText_gender;
    EditText editText_age;
    EditText editText_email;

    Boolean isCkeckPwd = false;

    Button reset_1;
    Button reset_2;

    Button addMember;
    Matcher matchTest;

    Member member;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kakao_register);

        member = (Member) getIntent().getSerializableExtra("kakaoMember");

        editText_phone = (EditText) findViewById(R.id.kakao_phone);
        editText_birth = (EditText) findViewById(R.id.kakao_birth);
        editText_gender = (EditText) findViewById(R.id.kakao_gender);
        editText_age = (EditText) findViewById(R.id.kakao_age);
        editText_email = (EditText) findViewById(R.id.kakao_email);

        if(!member.getEmail().equals("")) {
            editText_email.setText(member.getEmail());
            editText_email.setEnabled(false);
        }
        addMember = (Button) findViewById(R.id.kakao_btn_register);
        reset_1 = (Button) findViewById(R.id.kakao_reset_birth);
        reset_2 = (Button) findViewById(R.id.kakao_reset_phone);

        reset_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_birth.setText("");
            }
        });
        reset_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_phone.setText("");
            }
        });


        editText_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editText_phone.getText().toString().length()==11){
                    String str =editText_phone.getText().toString();
                    String result = str.substring(0,3).concat("-").concat(str.substring(3,7)).concat("-").concat(str.substring((7)));
                    editText_phone.setText(result);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        editText_birth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(editText_birth.getText().toString().length()==8){
                    String str =editText_birth.getText().toString();
                    String result = str.substring(0,4).concat("-").concat(str.substring(4,6)).concat("-").concat(str.substring((6)));
                    editText_birth.setText(result);
                }
            }
        });



        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText_phone.getText().toString().equals("") || !isPhone(editText_phone.getText().toString()) ||
                        editText_phone.getText().toString().length()!=13){
                    Toast.makeText(v.getContext(), "전화 번호를 제대로 입력하세요.", Toast.LENGTH_SHORT).show();
                }else if(editText_birth.getText().toString().equals("") || !validationDate(editText_birth.getText().toString())){
                    Toast.makeText(v.getContext(), "생년월일을 제대로 입력하세요.", Toast.LENGTH_SHORT).show();
                }else if(editText_gender.getText().toString().equals("") || !(editText_gender.getText().toString().toLowerCase().equals("male") || editText_gender.getText().toString().toLowerCase().equals("female"))){
                    Toast.makeText(v.getContext(), "성별을 제대로 입력하세요.", Toast.LENGTH_SHORT).show();
                }else if(editText_email.getText().toString().equals("") || !isEmail(editText_email.getText().toString())){
                    Toast.makeText(v.getContext(), "Email을 제대로 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    member.setPhoneNumber(editText_phone.getText().toString());
                    member.setBirth(editText_birth.getText().toString());
                    member.setGender(editText_gender.getText().toString());
                    member.setAge(Integer.parseInt(String.valueOf(editText_age.getText())));
                    member.setEmail(editText_email.getText().toString());

                    System.out.println("2 : " + member.toString());
                    if (member.isNUll()) {
                        Toast.makeText(v.getContext(), "내용을 제대로 입력하세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        member.insert(MainActivity_logout.connection);
                        Intent intent = new Intent();
                        intent.putExtra("Kakao", member);
                        setResult(RESULT_OK, intent);
                        Toast.makeText(v.getContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

            }
        });

    }

    public  boolean  validationDate(String checkDate){
        try{
            SimpleDateFormat  dateFormat = new  SimpleDateFormat("yyyy-MM-dd");

            dateFormat.setLenient(false);
            dateFormat.parse(checkDate);
            return  true;

        }catch (ParseException  e){
            return  false;
        }

    }
    public boolean isPhone(String str){
        String regExpPw = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";
        matchTest = Pattern.compile(regExpPw).matcher(str);
        if(matchTest.find()){
            return true;
        }else{
            return false;
        }
    }
    public boolean isEmail(String str){
        String regExpPw = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
        matchTest = Pattern.compile(regExpPw).matcher(str);
        if(matchTest.find()){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onBackPressed() {

    }

}

