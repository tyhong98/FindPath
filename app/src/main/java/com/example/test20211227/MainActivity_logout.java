package com.example.test20211227;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity_logout  extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {

    String API_Key = "l7xxa9a47470c4e34702a956688a5648a41c";

    private static String ip = "project-001.c0vrmramwje4.ap-northeast-2.rds.amazonaws.com"; //접속할 서버측의 IP, 현재는 로컬에서 진행하니 이 컴퓨터의 IP주소를 할당하면 된다.
    private static String port = "1433"; //SQL 구성 관리자에서 TCP/IP 구성 중 모든 IP포트를 설정하는 구간에서 동적 포트를 적으면 된다.
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "Android"; //접속할 데이터베이스 이름
    private static String username = "hyobin"; //서버의 ID PW
    private static String password = "1234";
    private static String url ="jdbc:jtds:sqlserver://"+ip+":"+port+"/"+database;
    public static Connection connection= null;
    // T Map View
    TMapView tMapView = null;
    static Member member;

    static NavigationView navigationView;
    // T Map GPS
    TMapGpsManager tMapGPS = null;

    Fragment fragment1;
    Fragment fragment2;
    Fragment fragment3;
    Fragment fragment4;
    static Intent intent;


    static ActionBar actionBar;
    static private DrawerLayout mDrawerLayout;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_logout);



        navigationView = (NavigationView) findViewById(R.id.navi_logout_menu_list);
        member= new Member();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName(Classes); //jdbc드라이버 클래스 적용
            connection = DriverManager.getConnection(url,username,password);
            System.out.println("연결성공");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


        // T Map View
        tMapView = new TMapView(this);

        // API Key
        tMapView.setSKTMapApiKey("l7xxa9a47470c4e34702a956688a5648a41c");

        // Initial Setting
        tMapView.setZoomLevel(17);
        tMapView.setIconVisibility(true);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);

        // T Map View Using Linear Layout
        LinearLayout linearLayoutTmap = (LinearLayout)findViewById(R.id.linearLayoutTmap);
        linearLayoutTmap.addView(tMapView);

        // Request For GPS permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        // GPS using T Map
        tMapGPS = new TMapGpsManager(this);

        // Initial Setting
        tMapGPS.setMinTime(1000);
        tMapGPS.setMinDistance(10);
        tMapGPS.setProvider(tMapGPS.NETWORK_PROVIDER);
        //tMapGPS.setProvider(tMapGPS.GPS_PROVIDER);

        tMapGPS.OpenGps();

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        fragment4 = new Fragment4();

        //getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment1).commit();
        // 초기화면 설정

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.tab_car:
                                show();
                                return true;
                            case R.id.tab_trafic:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment2).commit();
                                return true;

                            case R.id.tab_shop:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment3).commit();
                                return true;

                            case R.id.tab_bike:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment4).commit();
                                return true;
                        }
                        return false;
                    }
                });
        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.logout_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.view_menu_icon); //뒤로가기 버튼 이미지 지정

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                if(id == R.id.account_logout){
                    intent= new Intent(getApplicationContext(), LoginActivity.class);
                    startActivityForResult(intent,100);
                    Toast.makeText(context, title + " 화면 이동", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.issue_logout){
                    intent = new Intent(getApplicationContext(), IssueActivity.class);
                    String apiUrl = "http://openapi.seoul.go.kr:8088/6e7a4875736d6f6e39387254685051/xml/AccInfo/1/40/";
                    NoticeApi api = new NoticeApi(apiUrl);
                    ArrayList<Notice> list = api.select();
                    intent.putExtra("notice",list);
                    startActivity(intent);
                }
                return true;
            }
        });

    }


    public void button1(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity_logout.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 수정 내용.
        startActivity(intent);
    }


    @Override
    public void onLocationChange(Location location) {
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 왼쪽 상단 버튼 눌렀을 때
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 100) {
            member = (Member) data.getSerializableExtra("account");
            Intent intent = new Intent(getApplicationContext(),MainActivity_login.class);
            intent.putExtra("login",member);
            startActivity(intent);
            finish();
        }
    }
    void show() {
        AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity_logout.this);
        adb.setTitle("로그인이 필요한 서비스입니다.");
        adb.setMessage("로그인 페이지로 이동하시겠습니까?");

        adb.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        adb.setNegativeButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent= new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent,100);
            }
        });

        AlertDialog ad = adb.create();
        ad.show();
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity_logout.this);
        adb.setTitle("종료");
        adb.setMessage("Knock Knock을 종료하시겠습니까?");
        adb.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        adb.setNegativeButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity_logout.super.onBackPressed();
            }
        });

        AlertDialog ad = adb.create();
        ad.show();
    }
}