package com.example.test20211227;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
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

public class MemberModifyActivity extends Activity {
    EditText name;
    EditText gender;
    EditText birth;
    EditText phone;
    EditText email;

    EditText pass;
    EditText passCk;

    Button modify;
    Button passModify;
    Button cancel;

    Button reset_1;
    Button reset_2;

    Boolean isCkeckPwd;
    Matcher matchTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_modify_activity);

        name = (EditText) findViewById(R.id.memberModifyName);
        gender = (EditText) findViewById(R.id.memberModifyGender);
        birth = (EditText) findViewById(R.id.memberModifyBirth);
        phone = (EditText) findViewById(R.id.memberModifyPhone);
        email = (EditText) findViewById(R.id.memberModifyEmail);

        pass = (EditText) findViewById(R.id.modiftyPass);
        passCk = (EditText) findViewById(R.id.modiftyPassCk);

        modify = (Button) findViewById(R.id.memberModifyBtn);
        passModify = (Button) findViewById(R.id.modifyPassBtn);
        cancel = (Button) findViewById(R.id.modifyCancel);

        reset_1 = (Button) findViewById(R.id.member_reset_birth);
        reset_2 = (Button) findViewById(R.id.member_reset_phone);

        reset_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birth.setText("");
            }
        });
        reset_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone.setText("");
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            if(!pass.getText().toString().equals(passCk.getText().toString()) && !passCk.getText().toString().equals("")){
                    passCk.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("red")));
                    isCkeckPwd = false;
                }else{
                    passCk.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("gray")));
                    isCkeckPwd = true;
                }
            }
        });
        passCk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!pass.getText().toString().equals(passCk.getText().toString()) && !passCk.getText().toString().equals("")){
                    isCkeckPwd = false;
                }else{
                    isCkeckPwd = true;
                }
            }

        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(phone.getText().toString().length()==11){
                    String str =phone.getText().toString();
                    String result = str.substring(0,3).concat("-").concat(str.substring(3,7)).concat("-").concat(str.substring((7)));
                    phone.setText(result);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        birth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(birth.getText().toString().length()==8){
                    String str =birth.getText().toString();
                    String result = str.substring(0,4).concat("-").concat(str.substring(4,6)).concat("-").concat(str.substring((6)));
                    birth.setText(result);
                }
            }
        });
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!pass.getText().toString().equals(pass.getText().toString()) && !passCk.getText().toString().equals("")){
                    pass.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("red")));
                    isCkeckPwd = false;
                }else{
                    pass.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("gray")));
                    isCkeckPwd = true;
                }
            }
        });
        passCk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!passCk.getText().toString().equals(passCk.getText().toString()) && !passCk.getText().toString().equals("")){
                    passCk.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("red")));
                    isCkeckPwd = false;
                }else{
                    passCk.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("gray")));
                    isCkeckPwd = true;
                }
            }

        });
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().equals("")){
                    Toast.makeText(v.getContext(), "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                }else if(phone.getText().toString().equals("") || !isPhone(phone.getText().toString()) ||
                        phone.getText().toString().length()!=13){
                    Toast.makeText(v.getContext(), "전화 번호를 제대로 입력하세요.", Toast.LENGTH_SHORT).show();
                }else if(birth.getText().toString().equals("") || !validationDate(birth.getText().toString())){
                    Toast.makeText(v.getContext(), "생년월일을 제대로 입력하세요.", Toast.LENGTH_SHORT).show();
                }else if(gender.getText().toString().equals("") || !(gender.getText().toString().toLowerCase().equals("male") || gender.getText().toString().toLowerCase().equals("female"))){
                    Toast.makeText(v.getContext(), "성별을 제대로 입력하세요.", Toast.LENGTH_SHORT).show();
                }else if(email.getText().toString().equals("") || !isEmail(email.getText().toString())){
                    Toast.makeText(v.getContext(), "Email을 제대로 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    MainActivity_login.member.setNAME(name.getText().toString());
                    MainActivity_login.member.setGender(gender.getText().toString());
                    MainActivity_login.member.setBirth(birth.getText().toString());
                    MainActivity_login.member.setEmail(email.getText().toString());
                    MainActivity_login.member.setPhoneNumber(phone.getText().toString());

                    if (MainActivity_login.member.isNUll()) {
                        Toast.makeText(v.getContext(), "내용을 제대로 입력하세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        MainActivity_login.member.updateMember(MainActivity_logout.connection);
                        Toast.makeText(v.getContext(),"회원 정보가 수정되었습니다.",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
        passModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isCkeckPwd){
                    Toast.makeText(v.getContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }else if(!isPwd(pass.getText().toString())){
                    Toast.makeText(v.getContext(), "비밀번호를 다시 입력하세요.\n비밀번호는 8자 이상에 문자, 숫자, 특수문자를 포함해야 합니다. ", Toast.LENGTH_SHORT).show();
                }else{
                    MainActivity_login.member.setPWD(pass.getText().toString());
                    MainActivity_login.member.updatePassword(MainActivity_logout.connection);
                    Toast.makeText(v.getContext(),"비밀번호가 변경되었습니다.",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    public  boolean  validationDate(String checkDate){
        try{
            SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd");

            dateFormat.setLenient(false);
            dateFormat.parse(checkDate);
            return  true;

        }catch (ParseException e){
            return  false;
        }

    }
    public boolean isPwd(String str){
        String regExpPw = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9ㄱ-힣]).{8,20}$";
        matchTest = Pattern.compile(regExpPw).matcher(str);
        if(matchTest.find()){
            return true;
        }else{
            return false;
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

}
