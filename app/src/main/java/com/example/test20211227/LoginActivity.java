package com.example.test20211227;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    Button button1;
    Button button2;
    Button kakao_login_button;
    Button btn_google;

    EditText textId;
    EditText textPwd;

    Intent intent;
    Member member;

    private FirebaseAuth auth;  // 파이어 베이스 인증 객체
    private GoogleApiClient googleApiClient;    // 구글 API 클라이언트 객체
    private static final int REQ_SIGN_GOOGLE = 100; // 구글 로그인 결과 코드

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        textId = (EditText) findViewById(R.id.login_id);
        textPwd = (EditText) findViewById(R.id.login_pass);
        button1 = (Button) findViewById(R.id.btn_login);
        button2 = (Button) findViewById(R.id.btn_register);
        kakao_login_button = (Button) findViewById(R.id.kakao_login_button);
        btn_google = (Button) findViewById(R.id.btn_google);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Member member = new Member();
                if (textId.getText().toString().equals("")) {
                    Toast.makeText(v.getContext(), "ID를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (textPwd.getText().toString().equals("")) {
                    Toast.makeText(v.getContext(), "PWD를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    member = member.select(MainActivity_logout.connection, textId.getText().toString().trim());
                    if (member.isNUll()) {
                        Toast.makeText(v.getContext(), "존재하지 않는 ID 입니다.", Toast.LENGTH_SHORT).show();
                    } else if (!textPwd.getText().toString().equals(member.getPWD())) {
                        Toast.makeText(v.getContext(), "비밀번호 틀렸습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("account", member);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                v.getContext().startActivity(intent);
                Toast.makeText(v.getContext(), "회원가입창 이동", Toast.LENGTH_SHORT).show();
            }
        });

        kakao_login_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                accountLogin();

            }
        });

        // 구글 로그인
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        auth = FirebaseAuth.getInstance();  // 파이어베이스 인증 객체 초기화.

        btn_google = findViewById(R.id.btn_google);
        btn_google.setOnClickListener(new View.OnClickListener() {  // 구글 로그인 버튼을 클릭했을 때 이곳을 수행.
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, REQ_SIGN_GOOGLE);
            }
        });
    }
    public void accountLogin(){
        String TAG = "accountLogin()";
        UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this,(oAuthToken, error) -> {
            if (error != null) {
                Log.e(TAG, "로그인 실패", error);
            } else if (oAuthToken != null) {
                Log.i(TAG, "로그인 성공(토큰) : " + oAuthToken.getAccessToken());
                getUserInfo();
            }
            return null;
        });
    }
    public void getUserInfo(){
        member = new Member();
        String TAG = "getUserInfo()";
        UserApiClient.getInstance().me((user, meError) -> {
            if (meError != null) {
                Log.e(TAG, "사용자 정보 요청 실패", meError);
            } else {
                System.out.println("로그인 완료");

                // 로그인 한적 있을 때
                if(member.find(MainActivity_logout.connection,String.valueOf(user.getId()))){
                    member = member.select(MainActivity_logout.connection,String.valueOf(user.getId()));
                    Intent intent = new Intent();
                    intent.putExtra("account", member);
                    setResult(RESULT_OK, intent);
                    finish();

                // 로그인 한적 없을때
                }else{
                    member.setID(String.valueOf(user.getId()));
                    member.setNAME(user.getKakaoAccount().getProfile().getNickname());
                    member.setPWD("Kakao_Login");
                    intent= new Intent(getApplicationContext(), KakaoRegisterActivity.class);
                    intent.putExtra("kakaoMember",member);
                    startActivityForResult(intent,200);
                }
            }
            return null;
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {

            return;
        }
        if (requestCode == 200) {
            member = (Member) data.getSerializableExtra("Kakao");
            Intent intent = new Intent(getApplicationContext(),MainActivity_login.class);
            intent.putExtra("login",member);
            startActivity(intent);
            finish();
        }

        // 구글 로그인
        if(requestCode == REQ_SIGN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()) {    // 인증 결과가 성곡적이면...
                GoogleSignInAccount account = result.getSignInAccount();    // account 라는 데이터는 구글 로그인 정보를 담고 있습니다.(닉네임, 프로필사진Url, 이메일주소 등둥...)
                resultLogin(account);   // 로그인 결과 값 출력 수행하라는 메소드
            }
        }
    }

    private void resultLogin(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { // 로그인이 성공했으면...
                            // 로그인 한적 있을 때
                            member = new Member();

                            if(member.find(MainActivity_logout.connection,String.valueOf(account.getId()))){
                                member = member.select(MainActivity_logout.connection,String.valueOf(account.getId()));
                                Intent intent = new Intent();
                                intent.putExtra("account", member);
                                setResult(RESULT_OK, intent);
                                finish();

                                // 로그인 한적 없을때
                            }else{
                                member.setID(String.valueOf(account.getId()));
                                member.setNAME(account.getDisplayName());
                                member.setEmail(account.getEmail());
                                member.setPWD("Google_Login");
                                intent= new Intent(getApplicationContext(), KakaoRegisterActivity.class);
                                intent.putExtra("kakaoMember",member);
                                startActivityForResult(intent,200);
                            }
//                            if ()
//                                Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
//                                intent.putExtra("nickname",account.getDisplayName());
//                                intent.putExtra("photoUrl",String.valueOf(account.getPhotoUrl()));  // String.valueOf 특정 자료형을 String 형태로 변환.
//                                startActivity(intent);
                        } else {    // 로그인이 실패했으면...
//                            Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
//                            intent.putExtra("nickname",account.getDisplayName());
//                            intent.putExtra("photoUrl",String.valueOf(account.getPhotoUrl()));  // String.valueOf 특정 자료형을 String 형태로 변환.
//                            startActivity(intent);
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}


