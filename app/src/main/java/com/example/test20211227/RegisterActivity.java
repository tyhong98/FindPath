package com.example.test20211227;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends Activity {
    EditText editText_id;
    EditText editText_pwd;
    EditText editText_pwdCk;
    EditText editText_name;
    EditText editText_phone;
    EditText editText_birth;
    EditText editText_gender;
    EditText editText_age;
    EditText editText_email;

    Boolean isCkeckId = false;
    Boolean isCkeckPwd = false;

    Button idCk;
    Button reset_1;
    Button reset_2;
    Button addMember;
    Matcher matchTest;

    Member member;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        editText_id = (EditText) findViewById(R.id.et_id);
        editText_pwd = (EditText) findViewById(R.id.et_pass);
        editText_pwdCk = (EditText) findViewById(R.id.et_passck);
        editText_name = (EditText) findViewById(R.id.et_name);
        editText_phone = (EditText) findViewById(R.id.et_phone);
        editText_birth = (EditText) findViewById(R.id.et_birth);
        editText_gender = (EditText) findViewById(R.id.et_gender);
        editText_age = (EditText) findViewById(R.id.et_age);
        editText_email = (EditText) findViewById(R.id.et_email);

        idCk = (Button) findViewById(R.id.validateButton);
        addMember = (Button) findViewById(R.id.btn_register);
        reset_1 = (Button) findViewById(R.id.reset_birth);
        reset_2 = (Button) findViewById(R.id.reset_phone);

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
        editText_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!editText_pwd.getText().toString().equals(editText_pwdCk.getText().toString()) && !editText_pwdCk.getText().toString().equals("")){
                    editText_pwdCk.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("red")));
                    isCkeckPwd = false;
                }else{
                    editText_pwdCk.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("gray")));
                    isCkeckPwd = true;
                }
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
        editText_pwdCk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!editText_pwd.getText().toString().equals(editText_pwdCk.getText().toString()) && !editText_pwdCk.getText().toString().equals("")){
                    editText_pwdCk.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("red")));
                    isCkeckPwd = false;
                }else{
                    editText_pwdCk.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("gray")));
                    isCkeckPwd = true;
                }
            }

        });
        idCk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText_id.getText().toString().equals("")){
                    Toast.makeText(v.getContext(), "ID를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(editText_id.getText().toString().length()>16){
                    Toast.makeText(v.getContext(), "ID는 최대 15글자로 설정할 수 있습니다.", Toast.LENGTH_SHORT).show();
                }else if(editText_id.getText().toString().length()<4){
                    Toast.makeText(v.getContext(), "ID는 최소 4글자 이상으로 설정해야 합니다.", Toast.LENGTH_SHORT).show();
                }
                else if (!MainActivity_logout.member.find(MainActivity_logout.connection,editText_id.getText().toString())) {
                    Toast.makeText(v.getContext(), "사용 가능한 아이디 입니다.", Toast.LENGTH_SHORT).show();
                    isCkeckId = true;
                    editText_id.setEnabled(false);
                }
                else {
                    Toast.makeText(v.getContext(), "중복된 아이디가 존재합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                member = new Member();
                if(!isCkeckPwd){
                    Toast.makeText(v.getContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }else if(!isPwd(editText_pwd.getText().toString())){
                    Toast.makeText(v.getContext(), "비밀번호를 다시 입력하세요.\n비밀번호는 8자 이상에 문자, 숫자, 특수문자를 포함해야 합니다. ", Toast.LENGTH_SHORT).show();
                }else if(editText_name.getText().toString().equals("")){
                    Toast.makeText(v.getContext(), "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                }else if(editText_phone.getText().toString().equals("") || !isPhone(editText_phone.getText().toString()) ||
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
                    member.setID(editText_id.getText().toString());
                    member.setPWD(editText_pwd.getText().toString());
                    member.setPhoneNumber(editText_phone.getText().toString());
                    member.setNAME(editText_name.getText().toString());
                    member.setBirth(editText_birth.getText().toString());
                    member.setGender(editText_gender.getText().toString());
                    member.setAge(Integer.parseInt(String.valueOf(editText_age.getText())));
                    member.setEmail(editText_email.getText().toString());

                    if (!isCkeckId) {
                        Toast.makeText(v.getContext(), "ID 중복 확인을 해야 합니다.", Toast.LENGTH_SHORT).show();
                    } else if (member.isNUll()) {
                        Toast.makeText(v.getContext(), "내용을 제대로 입력하세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        member.insert(MainActivity_logout.connection);
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

