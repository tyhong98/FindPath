package com.example.test20211227;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
        import android.location.Location;
import android.os.Build;
import android.os.Bundle;
        import androidx.annotation.NonNull;
        import androidx.appcompat.widget.Toolbar;
        import androidx.core.view.GravityCompat;
        import androidx.drawerlayout.widget.DrawerLayout;
        import androidx.fragment.app.Fragment;
        import android.os.StrictMode;
        import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
        import com.google.android.material.navigation.NavigationView;
        import com.skt.Tmap.TMapGpsManager;
        import com.skt.Tmap.TMapView;

        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.SQLException;
        import java.util.ArrayList;

public class MainActivity_login extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {

    String API_Key = "l7xxa9a47470c4e34702a956688a5648a41c";
    // T Map View
    TMapView tMapView = null;
    static Member member;

    static NavigationView navigationView;
    // T Map GPS
    TMapGpsManager tMapGPS = null;
    boolean isit;
    Fragment fragment1;
    Fragment fragment2;
    Fragment fragment3;
    Fragment fragment4;
    static ActionBar actionBar;
    static private DrawerLayout mDrawerLayout;
    private Context context = this;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        navigationView = (NavigationView) findViewById(R.id.navi_login_menu_list);
        member = (Member) getIntent().getSerializableExtra("login");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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


        //getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment1).commit();
        // 초기화면 설정

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.tab_car:
                                fragment1 = new Fragment1();
                                isFrame1();
                                return true;
                            case R.id.tab_trafic:
                                fragment2 = new Fragment2();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment2).commit();
                                return true;

                            case R.id.tab_shop:
                                fragment3 = new Fragment3();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment3).commit();
                                return true;

                            case R.id.tab_bike:
                                fragment4 = new Fragment4();
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
                Intent intent;
                int id = menuItem.getItemId();

                if(id == R.id.member_info){
                    intent= new Intent(getApplicationContext(), MemberActivity.class);
                    startActivity(intent);
                }
                else if(id == R.id.delivery_info){
                    intent= new Intent(getApplicationContext(), DeliveryActivity.class);
                    ArrayList<Delivery> list = new ArrayList<>();
                    list.addAll(Delivery.select(MainActivity_logout.connection,MainActivity_login.member.getID()));
                    if(list.size()==0){ list.add(new Delivery("2","2","2","2","2"));}
                    intent.putExtra("DeliveryList",list);
                    startActivity(intent);
                }
                else if(id == R.id.issue_login){
                    intent = new Intent(getApplicationContext(), IssueActivity.class);
                    String apiUrl = "http://openapi.seoul.go.kr:8088/6e7a4875736d6f6e39387254685051/xml/AccInfo/1/40/";
                    NoticeApi api = new NoticeApi(apiUrl);
                    ArrayList<Notice> list = api.select();
                    intent.putExtra("notice",list);
                    startActivity(intent);
                }else if( id == R.id.logout_login){
                    intent = new Intent(getApplicationContext(),MainActivity_logout.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });


    }

    public void button1(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity_login.class);
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
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
            finish();
        }

    }
    public void isFrame1(){
        AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity_login.this);
        adb.setTitle("배달시작");
        adb.setMessage("배달을 시작하시겠습니까?");
        adb.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        adb.setNegativeButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();
            }
        });

        AlertDialog ad = adb.create();
        ad.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity_login.this);
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
                MainActivity_login.super.onBackPressed();
            }
        });

        AlertDialog ad = adb.create();
        ad.show();
    }
}