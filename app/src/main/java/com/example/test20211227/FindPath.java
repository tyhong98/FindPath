package com.example.test20211227;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.TMapView;

import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FindPath extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{

    public static Integer pos_number1;
    public static Integer pos_number2;
    private double a;
    private double b;
    private double c;
    private double d;
    private double AtoB;
    private double AtoC;
    private double AtoD;
    private double BtoC;
    private double BtoD;
    private double CtoD;
    private TMapPoint ALoc;
    private TMapPoint BLoc;
    private TMapPoint CLoc;
    private TMapPoint DLoc;
    private String Root;
    private int Limit;
    private int Index;


    //지도 변수 선언
    private LinearLayout linearLayoutTmap;
    private Context context;
    private TMapView tMapView;

    //검색창 관련 변수 선언
    private EditText inputStart_editText;
    private EditText inputEnd_editText;
    private EditText inputStart_editText2;
    private EditText inputEnd_editText2;
    private ListView searchResult_listView;
    private ListView searchResult_listView2;

    private List<String> list_data;
    private ArrayAdapter<String> adapter;
    private boolean display_listView = false;
    private int startLocation_finish = 0;

    private Button findPath_btn;
    private Button change_btn;
    private Button change_btn2;

    private boolean isCar;

    //이동수단 경로 선택 변수 선언
    private Button select_car_btn;
    private Button select_pedestrian_btn;
    private int findPath_case = 1;

    //TMap API 변수 선언
    private TMapData tMapData;

    //Marker 관련 변수 선언
    private Bitmap bitmap;
    private TMapPoint tMapPoint;
    private Marker startMarker;
    private Marker endMarker;
    private Marker startMarker2;
    private Marker endMarker2;
    private Marker curpositionMarker;

    //경로 안내 detail info 관련 변수 선언
    private Button detailInfo_path_btn;
    private Element root;

    //현위치 표시 관련 변수 선언
    private Button showCurPosition_btn;
    Bitmap bitmap2;
    TMapGpsManager tMapGpsManager;
    TMapPoint curPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findpath);
        TMapTapi tMapTapi = new TMapTapi(this);
        isCar = false;
        // Spinner
        Spinner Spinnerfood1 = (Spinner)findViewById(R.id.spinner_food1);
        ArrayAdapter food1 = ArrayAdapter.createFromResource(this, R.array.food1_list, android.R.layout.simple_spinner_item);
        food1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinnerfood1.setAdapter(food1);
        Spinnerfood1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer pos1 = (Integer) Spinnerfood1.getSelectedItemPosition();
                pos_number1 = pos1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        Spinner Spinnerfood2 = (Spinner)findViewById(R.id.spinner_food2);
        ArrayAdapter<CharSequence> food2 = ArrayAdapter.createFromResource(this, R.array.food2_list, android.R.layout.simple_spinner_item);
        food2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinnerfood2.setAdapter(food2);
        Spinnerfood2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer pos2 = (Integer) Spinnerfood2.getSelectedItemPosition();
                pos_number2 = pos2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        //------------------------ 변수 선언 ------------------------//

        //검색창 변수 설정
        inputStart_editText = (EditText) findViewById(R.id.inputStart_editText);
        inputEnd_editText = (EditText) findViewById(R.id.inputEnd_editText);
        inputStart_editText2 = (EditText) findViewById(R.id.inputStart_editText2);
        inputEnd_editText2 = (EditText) findViewById(R.id.inputEnd_editText2);
        searchResult_listView = (ListView)findViewById(R.id.searchResultList);
        searchResult_listView2 = (ListView)findViewById(R.id.searchResultList);

        list_data = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_data);
        searchResult_listView.setAdapter(adapter);
        searchResult_listView2.setAdapter(adapter);

        findPath_btn = (Button)findViewById(R.id.findPath_btn);
        change_btn2 = (Button)findViewById(R.id.change_btn2);
        change_btn = (Button)findViewById(R.id.change_btn);

        //지도 변수 설정
        linearLayoutTmap = (LinearLayout)findViewById(R.id.linearLayoutTmap);
        context = this;
        tMapView = new TMapView(context);

        //TMap API 변수 설정
        tMapData = new TMapData();

        //Marker 관련 변수 설정
        curpositionMarker = new Marker();
        startMarker = new Marker();
        endMarker = new Marker();
        startMarker2 = new Marker();
        endMarker2 = new Marker();
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.poi_dot);
        //bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.curposition);
        tMapPoint = new TMapPoint(0,0);




        //이동수단 경로 선택 변수 선언
        select_car_btn = (Button)findViewById(R.id.select_car_btn);
        select_pedestrian_btn = (Button)findViewById(R.id.select_pedestrian_btn);
        select_pedestrian_btn.setSelected(true);


        //현위치 표시 관련 변수 설정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        tMapGpsManager = new TMapGpsManager(this);
        tMapGpsManager.setProvider(tMapGpsManager.NETWORK_PROVIDER);
        tMapGpsManager.OpenGps();


        //지도 설정
        context = this;
        tMapView = new TMapView(context);
        tMapView.setHttpsMode(true);
        tMapView.setSKTMapApiKey( "l7xx56f6c527fe08492f94c27fa47f861229" );
        linearLayoutTmap.addView( tMapView );


        //검색창 입력 이벤트 설정 text_1
        inputStart_editText.addTextChangedListener(new TextWatcher() {
            String input_locationPOI;
            //입력하기 전 이벤트
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            //입력할 때  이벤트
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s == null) return;

                if(!display_listView){
                    startLocation_finish = 1;
                    linearLayoutTmap.setVisibility(View.INVISIBLE);
                    searchResult_listView.setVisibility(View.VISIBLE);
                    display_listView = true;
                }

                //ListView 내용 초기화
                list_data.clear();

                if(s.toString().length() < 2){ //2개 미만은 검색 X
                    adapter.notifyDataSetChanged();
                    return;
                }

                input_locationPOI = s.toString();

                tMapData.findAllPOI(input_locationPOI, 10, new TMapData.FindAllPOIListenerCallback() {
                    @Override
                    public void onFindAllPOI(ArrayList<TMapPOIItem> arrayList) {
                        if(!arrayList.isEmpty()){
                            for(int i = 0; i < arrayList.size(); i++) {
                                TMapPOIItem item = arrayList.get(i);
                                list_data.add(item.getPOIName());
                            }
                            //새로운 Thread 생성
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable(){
                                        @Override
                                        public void run() {
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            }).start();
                        }
                    }
                });
            }
            //입력 후 이벤트
            @Override
            public void afterTextChanged(Editable s) { }
        });
        //text_2
        inputEnd_editText.addTextChangedListener(new TextWatcher() {
            String input_locationPOI;
            //입력하기 전 이벤트
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            //입력할 때  이벤트
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s == null) return;

                //ListView 내용 초기화
                list_data.clear();
                if(!display_listView){
                    startLocation_finish = 2;
                    linearLayoutTmap.setVisibility(View.INVISIBLE);
                    searchResult_listView.setVisibility(View.VISIBLE);
                    display_listView = true;
                }

                if(s.toString().length() < 2){ //2개 미만은 검색 X
                    adapter.notifyDataSetChanged();
                    return;
                }

                input_locationPOI = s.toString();

                tMapData.findAllPOI(input_locationPOI, 10, new TMapData.FindAllPOIListenerCallback() {
                    @Override
                    public void onFindAllPOI(ArrayList<TMapPOIItem> arrayList) {
                        if(!arrayList.isEmpty()){
                            for(int i = 0; i < arrayList.size(); i++) {
                                TMapPOIItem item = arrayList.get(i);
                                list_data.add(item.getPOIName());
                            }
                            //새로운 Thread 생성
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable(){
                                        @Override
                                        public void run() {
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            }).start();
                        }
                    }
                });
            }
            //입력 후 이벤트
            @Override
            public void afterTextChanged(Editable s) { }
        });

        //here text_3
        //검색창 입력 이벤트 설정
        inputStart_editText2.addTextChangedListener(new TextWatcher() {
            String input_locationPOI;
            //입력하기 전 이벤트
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            //입력할 때  이벤트
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s == null) return;

                if(!display_listView){
                    startLocation_finish = 3;
                    linearLayoutTmap.setVisibility(View.INVISIBLE);
                    searchResult_listView2.setVisibility(View.VISIBLE);
                    display_listView = true;
                }

                //ListView 내용 초기화
                list_data.clear();

                if(s.toString().length() < 2){ //2개 미만은 검색 X
                    adapter.notifyDataSetChanged();
                    return;
                }

                input_locationPOI = s.toString();

                tMapData.findAllPOI(input_locationPOI, 10, new TMapData.FindAllPOIListenerCallback() {
                    @Override
                    public void onFindAllPOI(ArrayList<TMapPOIItem> arrayList) {
                        if(!arrayList.isEmpty()){
                            for(int i = 0; i < arrayList.size(); i++) {
                                TMapPOIItem item = arrayList.get(i);
                                list_data.add(item.getPOIName());
                            }
                            //새로운 Thread 생성
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable(){
                                        @Override
                                        public void run() {
                                            adapter.notifyDataSetChanged();
                                        }

                                    });
                                }
                            }).start();
                        }
                    }
                });
            }
            //입력 후 이벤트
            @Override
            public void afterTextChanged(Editable s) { }
        });
        //text_4
        inputEnd_editText2.addTextChangedListener(new TextWatcher() {
            String input_locationPOI;
            //입력하기 전 이벤트
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            //입력할 때  이벤트
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s == null) return;

                //ListView 내용 초기화
                list_data.clear();
                if(!display_listView){
                    startLocation_finish = 4;
                    linearLayoutTmap.setVisibility(View.INVISIBLE);
                    searchResult_listView2.setVisibility(View.VISIBLE);
                    display_listView = true;
                }

                if(s.toString().length() < 2){ //2개 미만은 검색 X
                    adapter.notifyDataSetChanged();
                    return;
                }

                input_locationPOI = s.toString();

                tMapData.findAllPOI(input_locationPOI, 10, new TMapData.FindAllPOIListenerCallback() {
                    @Override
                    public void onFindAllPOI(ArrayList<TMapPOIItem> arrayList) {
                        if(!arrayList.isEmpty()){
                            for(int i = 0; i < arrayList.size(); i++) {
                                TMapPOIItem item = arrayList.get(i);
                                list_data.add(item.getPOIName());
                            }
                            //새로운 Thread 생성
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable(){
                                        @Override
                                        public void run() {
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            }).start();
                        }
                    }
                });
            }
            //입력 후 이벤트
            @Override
            public void afterTextChanged(Editable s) { }
        });

        //검색창 클릭 이벤트 설정
        searchResult_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Marker marker;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tMapData.findAllPOI(list_data.get(position), 1, new TMapData.FindAllPOIListenerCallback() {
                    @Override
                    public void onFindAllPOI(ArrayList<TMapPOIItem> arrayList) {

                        TMapPOIItem item;
                        item = arrayList.get(0);
                        marker = new Marker(item.getPOIName(), item.getPOIAddress(), "None",item.getPOIPoint().getLatitude(), item.getPOIPoint().getLongitude());

                        if (startLocation_finish == 1) {
                            inputStart_editText.setText(marker.getName());
                            startMarker = marker;
                            TMapPoint test = new TMapPoint(tMapGpsManager.getLocation().getLatitude(), tMapGpsManager.getLocation().getLongitude());
                            a = item.getDistance(test);
                            ALoc = new TMapPoint(item.getPOIPoint().getLatitude(),item.getPOIPoint().getLongitude());
                        }else if(startLocation_finish == 2){
                            inputEnd_editText.setText(marker.getName());
                            endMarker = marker;
                            TMapPoint test = new TMapPoint(tMapGpsManager.getLocation().getLatitude(), tMapGpsManager.getLocation().getLongitude());
                            b = item.getDistance(test);
                            BLoc = new TMapPoint(item.getPOIPoint().getLatitude(),item.getPOIPoint().getLongitude());
                            AtoB = item.getDistance(ALoc);
                        }else if(startLocation_finish == 3){
                            inputStart_editText2.setText(marker.getName());
                            startMarker2 = marker;
                            TMapPoint test = new TMapPoint(tMapGpsManager.getLocation().getLatitude(), tMapGpsManager.getLocation().getLongitude());
                            c = item.getDistance(test);
                            CLoc = new TMapPoint(item.getPOIPoint().getLatitude(),item.getPOIPoint().getLongitude());
                            AtoC = item.getDistance(ALoc);
                            if(BLoc!=null){
                                BtoC = item.getDistance(BLoc);
                            }
                        }else if(startLocation_finish == 4){
                            inputEnd_editText2.setText(marker.getName());
                            endMarker2 = marker;
                            TMapPoint test = new TMapPoint(tMapGpsManager.getLocation().getLatitude(), tMapGpsManager.getLocation().getLongitude());
                            d = item.getDistance(test);
                            DLoc = new TMapPoint(item.getPOIPoint().getLatitude(),item.getPOIPoint().getLongitude());
                            AtoD = item.getDistance(ALoc);
                            BtoD = item.getDistance(BLoc);
                            CtoD = item.getDistance(CLoc);
                        }

                        //카메라 이동
                        tMapView.setCenterPoint(marker.getLongitude(),marker.getLatitude());

                        //Point 좌표 설정
                        tMapPoint.setLongitude(marker.getLongitude());
                        tMapPoint.setLatitude(marker.getLatitude());

                        TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();

                        tMapMarkerItem.setIcon(bitmap); // 마커 아이콘 지정
                        tMapMarkerItem.setCanShowCallout(true);
                        tMapMarkerItem.setAutoCalloutVisible(true);
                        tMapMarkerItem.setTMapPoint(tMapPoint); //마커 좌표 설정

                        //마커 Title & SubTitle 지정
                        tMapMarkerItem.setCalloutTitle(marker.getName());
                        String getad  = marker.getAddress();
                        int len = getad.length();
                        String getAd = getad.substring(0,len-4);
                        tMapMarkerItem.setCalloutSubTitle(getAd);

                        if(startLocation_finish==1){
                            startMarker.setMarker_id("markerItem_1");
                            tMapView.addMarkerItem(startMarker.getMarker_id(), tMapMarkerItem); // 지도에 마커 추가
                        }else if(startLocation_finish==2){
                            endMarker.setMarker_id("markerItem_2");
                            tMapView.addMarkerItem(endMarker.getMarker_id(), tMapMarkerItem); // 지도에 마커 추가
                        }else if(startLocation_finish==3){
                            startMarker2.setMarker_id("markerItem_2_1");
                            tMapView.addMarkerItem(startMarker2.getMarker_id(), tMapMarkerItem); // 지도에 마커 추가
                        }else if(startLocation_finish==4){
                            endMarker2.setMarker_id("markerItem_2_2");
                            tMapView.addMarkerItem(endMarker2.getMarker_id(), tMapMarkerItem); // 지도에 마커 추가
                        }

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable(){
                                    @Override
                                    public void run() {
                                        // 해당 작업을 처리함
                                        linearLayoutTmap.setVisibility(View.VISIBLE);
                                        searchResult_listView.setVisibility(View.INVISIBLE);
                                        display_listView = false;
                                    }
                                });
                            }
                        }).start();
                    }
                });
            }
        });

        findPath_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //카메라 중심정 이동
                curPosition = new TMapPoint(tMapGpsManager.getLocation().getLatitude(), tMapGpsManager.getLocation().getLongitude());

                tMapView.setCenterPoint(curPosition.getLongitude(),curPosition.getLatitude(),false);
                tMapView.setZoomLevel(14);


                //경로 안내
                switch (findPath_case){
                    case 0:     //자동차
                        Limit = 3000;
                        FindPath_Car(Limit);
                        break;
                    case 1:     //도보
                        Limit = 750;
                        FindPath_Pedestrian(Limit);
                        break;
                }
            }
        });

        //Tmap실행
        change_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (endMarker.getName().length() <= 2) {
                    //가게와 거주지 1개씩 있을 경우
                    HashMap pathInfo = new HashMap();
                    pathInfo.put("rGoName", startMarker2.getName()); //도착지
                    pathInfo.put("rGoX", Double.toString(startMarker2.getLongitude()));
                    pathInfo.put("rGoY", Double.toString(startMarker2.getLatitude()));

                    pathInfo.put("rV1Name", startMarker.getName()); //경유지
                    pathInfo.put("rV1X", Double.toString(startMarker.getLongitude()));
                    pathInfo.put("rV1Y", Double.toString(startMarker.getLatitude()));

                    tMapTapi.invokeRoute(pathInfo);
                } else {
                    //가게와 거주지 2개씩 있을 경우
                    switch (findPath_case){
                        case 0:     //자동차
                            Limit = 3000;
                            break;
                        case 1:    //도보
                            Limit = 750;
                            break;
                    }
                    PathPlusFood(Limit);
                    if(Index == 0){ //가게1 > 목적지1 > 가게2 > 목적지2

                        HashMap pathInfo = new HashMap();
                        pathInfo.put("rGoName", startMarker2.getName()); //도착지
                        pathInfo.put("rGoX", Double.toString(startMarker2.getLongitude()));
                        pathInfo.put("rGoY", Double.toString(startMarker2.getLatitude()));

                        pathInfo.put("rV1Name", startMarker.getName()); //경유지1
                        pathInfo.put("rV1X", Double.toString(startMarker.getLongitude()));
                        pathInfo.put("rV1Y", Double.toString(startMarker.getLatitude()));

                        tMapTapi.invokeRoute(pathInfo);

                        change_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                HashMap pathInfo = new HashMap();
                                pathInfo.put("rGoName", endMarker2.getName()); //도착지
                                pathInfo.put("rGoX", Double.toString(endMarker2.getLongitude()));
                                pathInfo.put("rGoY", Double.toString(endMarker2.getLatitude()));

                                pathInfo.put("rV1Name", endMarker.getName()); //경유지1
                                pathInfo.put("rV1X", Double.toString(endMarker.getLongitude()));
                                pathInfo.put("rV1Y", Double.toString(endMarker.getLatitude()));

                                tMapTapi.invokeRoute(pathInfo);

                            }
                        });
                    }else if(Index == 1){ //가게1 > 가게2 > 목적지1 > 목적지2

                        HashMap pathInfo = new HashMap();
                        pathInfo.put("rGoName", endMarker.getName()); //도착지
                        pathInfo.put("rGoX", Double.toString(endMarker.getLongitude()));
                        pathInfo.put("rGoY", Double.toString(endMarker.getLatitude()));

                        pathInfo.put("rV1Name", startMarker.getName()); //경유지1
                        pathInfo.put("rV1X", Double.toString(startMarker.getLongitude()));
                        pathInfo.put("rV1Y", Double.toString(startMarker.getLatitude()));

                        tMapTapi.invokeRoute(pathInfo);

                        change_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                HashMap pathInfo = new HashMap();
                                pathInfo.put("rGoName", endMarker2.getName()); //도착지
                                pathInfo.put("rGoX", Double.toString(endMarker2.getLongitude()));
                                pathInfo.put("rGoY", Double.toString(endMarker2.getLatitude()));

                                pathInfo.put("rV1Name", startMarker2.getName()); //경유지1
                                pathInfo.put("rV1X", Double.toString(startMarker2.getLongitude()));
                                pathInfo.put("rV1Y", Double.toString(startMarker2.getLatitude()));

                                tMapTapi.invokeRoute(pathInfo);

                            }
                        });
                    }else if(Index == 2){ //가게1 > 가게2 > 목적지2 > 목적지1

                        HashMap pathInfo = new HashMap();
                        pathInfo.put("rGoName", endMarker.getName()); //도착지
                        pathInfo.put("rGoX", Double.toString(endMarker.getLongitude()));
                        pathInfo.put("rGoY", Double.toString(endMarker.getLatitude()));

                        pathInfo.put("rV1Name", startMarker.getName()); //경유지1
                        pathInfo.put("rV1X", Double.toString(startMarker.getLongitude()));
                        pathInfo.put("rV1Y", Double.toString(startMarker.getLatitude()));

                        tMapTapi.invokeRoute(pathInfo);

                        change_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                HashMap pathInfo = new HashMap();
                                pathInfo.put("rGoName", startMarker2.getName()); //도착지
                                pathInfo.put("rGoX", Double.toString(startMarker2.getLongitude()));
                                pathInfo.put("rGoY", Double.toString(startMarker2.getLatitude()));

                                pathInfo.put("rV1Name", endMarker2.getName()); //경유지1
                                pathInfo.put("rV1X", Double.toString(endMarker2.getLongitude()));
                                pathInfo.put("rV1Y", Double.toString(endMarker2.getLatitude()));

                                tMapTapi.invokeRoute(pathInfo);

                            }
                        });
                    }else if(Index == 3){ //가게2 > 가게1 > 목적지1 > 목적지2

                        HashMap pathInfo = new HashMap();
                        pathInfo.put("rGoName", startMarker.getName()); //도착지
                        pathInfo.put("rGoX", Double.toString(startMarker.getLongitude()));
                        pathInfo.put("rGoY", Double.toString(startMarker.getLatitude()));

                        pathInfo.put("rV1Name", endMarker.getName()); //경유지1
                        pathInfo.put("rV1X", Double.toString(endMarker.getLongitude()));
                        pathInfo.put("rV1Y", Double.toString(endMarker.getLatitude()));

                        tMapTapi.invokeRoute(pathInfo);

                        change_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                HashMap pathInfo = new HashMap();
                                pathInfo.put("rGoName", endMarker2.getName()); //도착지
                                pathInfo.put("rGoX", Double.toString(endMarker2.getLongitude()));
                                pathInfo.put("rGoY", Double.toString(endMarker2.getLatitude()));

                                pathInfo.put("rV1Name", startMarker2.getName()); //경유지1
                                pathInfo.put("rV1X", Double.toString(startMarker2.getLongitude()));
                                pathInfo.put("rV1Y", Double.toString(startMarker2.getLatitude()));

                                tMapTapi.invokeRoute(pathInfo);

                            }
                        });
                    }else if(Index == 4){ //가게2 > 가게1 > 목적지2 > 목적지1

                        HashMap pathInfo = new HashMap();
                        pathInfo.put("rGoName", startMarker.getName()); //도착지
                        pathInfo.put("rGoX", Double.toString(startMarker.getLongitude()));
                        pathInfo.put("rGoY", Double.toString(startMarker.getLatitude()));

                        pathInfo.put("rV1Name", endMarker.getName()); //경유지1
                        pathInfo.put("rV1X", Double.toString(endMarker.getLongitude()));
                        pathInfo.put("rV1Y", Double.toString(endMarker.getLatitude()));

                        tMapTapi.invokeRoute(pathInfo);

                        change_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                HashMap pathInfo = new HashMap();
                                pathInfo.put("rGoName", startMarker2.getName()); //도착지
                                pathInfo.put("rGoX", Double.toString(startMarker2.getLongitude()));
                                pathInfo.put("rGoY", Double.toString(startMarker2.getLatitude()));

                                pathInfo.put("rV1Name", endMarker2.getName()); //경유지1
                                pathInfo.put("rV1X", Double.toString(endMarker2.getLongitude()));
                                pathInfo.put("rV1Y", Double.toString(endMarker2.getLatitude()));

                                tMapTapi.invokeRoute(pathInfo);

                            }
                        });
                    }else { //가게2 > 목적지2 > 가게1 > 목적지1

                        HashMap pathInfo = new HashMap();
                        pathInfo.put("rGoName", endMarker2.getName()); //도착지
                        pathInfo.put("rGoX", Double.toString(endMarker2.getLongitude()));
                        pathInfo.put("rGoY", Double.toString(endMarker2.getLatitude()));

                        pathInfo.put("rV1Name", endMarker.getName()); //경유지1
                        pathInfo.put("rV1X", Double.toString(endMarker.getLongitude()));
                        pathInfo.put("rV1Y", Double.toString(endMarker.getLatitude()));

                        tMapTapi.invokeRoute(pathInfo);

                        change_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                HashMap pathInfo = new HashMap();
                                pathInfo.put("rGoName", startMarker2.getName()); //도착지
                                pathInfo.put("rGoX", Double.toString(startMarker2.getLongitude()));
                                pathInfo.put("rGoY", Double.toString(startMarker2.getLatitude()));

                                pathInfo.put("rV1Name", startMarker.getName()); //경유지1
                                pathInfo.put("rV1X", Double.toString(startMarker.getLongitude()));
                                pathInfo.put("rV1Y", Double.toString(startMarker.getLatitude()));

                                tMapTapi.invokeRoute(pathInfo);

                            }
                        });
                    }
                    //boolean av = tMapTapi.invokeNavigate("", (float)127.14823935774831, (float)37.55311904812474, 0, true);

                }
            }
        });

        //이동수단 버튼 클릭 이벤트 설정
        //자동차
        select_car_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_car_btn.setSelected(true); //선택한 버튼 클릭 상태로 유지
                SelectTransPort(0); //그 전에 선택한 버튼 클릭 상태 해제 및 findPath_case 값 변경
                isCar=true;
            }
        });
        //보행자
        select_pedestrian_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_pedestrian_btn.setSelected(true); //선택한 버튼 클릭 상태로 유지
                SelectTransPort(1); //그 전에 선택한 버튼 클릭 상태 해제 및 findPath_case 값 변경
                isCar=false;
            }
        });

        Button finishbutton = (Button) findViewById(R.id.finish_btn);
        finishbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(FindPath.this)
                        .setTitle("안내 종료")
                        .setMessage("안내를 종료하시겠습니까?")
                        .setNegativeButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(String.valueOf(inputEnd_editText.getText()).equals("") && String.valueOf(inputEnd_editText2.getText()).equals("") &&
                                        !(String.valueOf(inputStart_editText.getText()).equals("")) && !(String.valueOf(inputStart_editText2.getText()).equals(""))){
                                    Delivery delivery = new Delivery();
                                    delivery.setFoodName(String.valueOf(Spinnerfood1.getSelectedItem()));
                                    delivery.setDeliveryAddress(String.valueOf(inputStart_editText2.getText()));
                                    delivery.setRestaurantAddress(String.valueOf(inputStart_editText.getText()));
                                    if(isCar) {
                                        delivery.setMeans("차");
                                    }else{
                                        delivery.setMeans("도보");
                                    }
                                    delivery.setEtc("...");
                                    delivery.insert(MainActivity_logout.connection,MainActivity_login.member.getID());
                                    finish();
                                }else if (!(String.valueOf(inputStart_editText.getText()).equals("")) && !(String.valueOf(inputEnd_editText.getText()).equals(""))
                                        && !(String.valueOf(inputStart_editText2.getText()).equals("")) && !(String.valueOf(inputEnd_editText2.getText()).equals(""))){
                                    Delivery delivery1 = new Delivery();
                                    Delivery delivery2 = new Delivery();

                                    delivery1.setFoodName(String.valueOf(Spinnerfood1.getSelectedItem()));
                                    delivery1.setDeliveryAddress(String.valueOf(inputStart_editText2.getText()));
                                    delivery1.setRestaurantAddress(String.valueOf(inputStart_editText.getText()));

                                    delivery2.setFoodName(String.valueOf(Spinnerfood2.getSelectedItem()));
                                    delivery2.setDeliveryAddress(String.valueOf(inputEnd_editText2.getText()));
                                    delivery2.setRestaurantAddress(String.valueOf(inputEnd_editText.getText()));
                                    if(isCar) {
                                        delivery1.setMeans("차");
                                        delivery2.setMeans("차");
                                    }else{
                                        delivery1.setMeans("도보");
                                        delivery2.setMeans("도보");
                                    }
                                    delivery1.setEtc("...");
                                    delivery2.setEtc("...");
                                    delivery1.insert(MainActivity_logout.connection,MainActivity_login.member.getID());
                                    delivery2.insert(MainActivity_logout.connection,MainActivity_login.member.getID());
                                    finish();
                                }else{
                                    Toast.makeText(FindPath.this,"배달 정보를 정확히 입력해주세요.",Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setPositiveButton("아니요", null)
                        .setIcon(R.drawable.tmap_img)
                        .show();
            }
        });

    }

    public void PathPlusFood(int Limit){

        if(a<=b){ //1.  가게1, 가게2 우선순위(가게1이 더 가까울 경우)
            if(BtoC <= Limit){ //가게2와 목적지1, 2km 이내일 경우
                if(pos_number1 < pos_number2) { //음식으로 우선순위, 들렸던 가게1의 음식이 우선일 경우 목적지1로 이동

                    //가게1 > 목적지1 > 가게2 > 목적지2
                    Root = "가게1 > 목적지1 > 가게2 > 목적지2";
                    Index = 0;
                }else if(pos_number1 == pos_number2){ //음식 우선이 같은 경우 거리순
                    if(AtoB<=AtoC){//가게2가 더 가까울 경우
                        //가게1 > 가게2
                        if(BtoC<=BtoD){ //목적지1이 더 가까울 경우
                            //가게1 > 가게2 > 목적지1 > 목적지2
                            Root = "가게1 > 가게2 > 목적지1 > 목적지2";
                            Index = 1;
                        }else{ //목적지2가 더 가까울 경우
                            //가게1 > 가게2 > 목적지2 > 목적지1
                            Root = "가게1 > 가게2> 목적지2 > 목적지1";
                            Index = 2;
                        }
                    }else{//목적지1이 더 가까울 경우

                        //가게1 > 목적지 1 > 가게2 > 목적지 2
                        Root = "가게1 > 목적지1 > 가게2 > 목적지2";
                        Index = 0;
                    }
                }else{  //가게2의 음식이 우선일 경우, 가게1 > 가게2
                    if(CtoD <= Limit){ //목적지1과 목적지2 비교, 2km 이내일 경우
                        //가게1 > 가게2 > 목적지2 > 목적지1
                        Root = "가게1 > 가게2> 목적지2 > 목적지1";
                        Index = 2;
                    }else{ //목적지1과 목적지2 비교, 2km 이상일 경우, 거리가 우선
                        if(BtoC<=BtoD){ //목적지1이 가까울 경우
                            // 가게1 > 가게2 > 목적지1 > 목적지2
                            Root = "가게1 > 가게2 > 목적지1 > 목적지2";
                            Index = 1;
                        }else{ //목적지2가 더 가까울 경우
                            //가게1 > 가게2 > 목적지2 > 목적지1
                            Root = "가게1 > 가게2 > 목적지2 > 목적지1";
                            Index = 2;
                        }
                    }
                }
            }else { //가게2와 목적지1, 2km 이상일 경우
                //가게1 > 목적지 1 > 가게2 > 목적지 2
                Root = "가게1 > 목적지1 > 가게2 > 목적지2";
                Index = 0;
            }
        }else{ //1. 가게1, 가게2 우선순위(가게2가 더 가까울 경우)
            if(AtoD <= Limit) { //가게1와 목적지2, 2km 이내일 경우, 음식 우선순위
                if(pos_number1 < pos_number2){ //가게1 음식이 우선
                    //가게2 > 가게1
                    if(CtoD <= Limit){ //목적지1 목적지2, 2km 이내일 경우, 음식 우선순위
                        //가게2 > 가게1 > 목적지1 > 목적지2
                        Root = "가게2 > 가게1 > 목적지1 > 목적지2";
                        Index = 3;
                    }else{ //목적지1 목적지2, 2km 이상일 경우, 거리 우선순위
                        if(AtoC<=AtoD){ //목적지1이 더 가까울 경우
                            //가게2 > 가게1 > 목적지1 > 목적지2
                            Root = "가게2 > 가게1 > 목적지1 > 목적지2";
                            Index = 3;
                        }else{ //목적지2가 더 가까울 경우
                            //가게2 > 가게1 > 목적지2 > 목적지1
                            Root = "가게2 > 가게1 > 목적지2 > 목적지1";
                            Index = 4;
                        }
                    }
                }else if(pos_number1 == pos_number2){ //가게1 목적지2 음식우선순위 같을 경우
                    //거리순
                    if(AtoB<=BtoD){ //가게1이 가까움
                        //가게2 > 가게1
                        if(AtoC<=AtoD){ //목적지1이 가까움
                            //가게2 > 가게1 > 목적지1 > 목적지2
                            Root = "가게2 > 가게1 > 목적지1 > 목적지2";
                            Index = 3;

                        }else{ //목적지2가 가까움
                            //가게2 > 가게1 > 목적지2 > 목적지1
                            Root = "가게2 > 가게1 > 목적지2 > 목적지1";
                            Index = 4;
                        }
                    }else{ //목적지2가 가까움
                        //가게2 > 목적지2 > 가게1 > 목적지1
                        Root = "가게2 > 목적지2 > 가게1 > 목적지1";
                        Index = 5;
                    }
                }else{ //목적지2 음식이 우선
                    //가게2 > 목적지2 > 가게1 > 목적지1
                    Root = "가게2 > 목적지2 > 가게1 > 목적지1";
                    Index = 5;
                }
            }else{ //가게1와 목적지2, 2km 이상일 경우, 거리 우선순위
                //가게2 > 목적지2 > 가게1 > 목적지1
                Root = "가게2 > 목적지2 > 가게1 > 목적지1";
                Index = 5;
            }
        }
    }

    //polyline
    //보행자 경로 찾는 함수
    public void FindPath_Pedestrian(int Limit){
        curPosition = new TMapPoint(tMapGpsManager.getLocation().getLatitude(), tMapGpsManager.getLocation().getLongitude());
        PathPlusFood(Limit);
        if(endMarker.getName().length()<=2){ //가게 목적지 하나씩 일 경우
            TMapPoint startpoint = new TMapPoint(startMarker.getLatitude(),startMarker.getLongitude());
            TMapPoint startpoint2 = new TMapPoint(startMarker2.getLatitude(),startMarker2.getLongitude());
            ArrayList<TMapPoint> passList = new ArrayList<>();
            passList.add(startpoint);

            tMapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, curPosition, startpoint2,  passList,
                    0, new TMapData.FindPathDataListenerCallback() {
                        @Override
                        public void onFindPathData(TMapPolyLine tMapPolyLine) {
                            tMapPolyLine.setLineColor(Color.BLUE);
                            tMapView.addTMapPath(tMapPolyLine);
                        }
                    });
        }else{
            TMapPoint startpoint = new TMapPoint(startMarker.getLatitude(), startMarker.getLongitude());
            TMapPoint endpoint = new TMapPoint(endMarker.getLatitude(), endMarker.getLongitude());
            TMapPoint startpoint2 = new TMapPoint(startMarker2.getLatitude(), startMarker2.getLongitude());
            TMapPoint endpoint2 = new TMapPoint(endMarker2.getLatitude(), endMarker2.getLongitude());
            if (Index == 0) { //가게1 > 목적지1 > 가게2 > 목적지2
                ArrayList<TMapPoint> passList = new ArrayList<>();
                passList.add(startpoint);
                passList.add(startpoint2);
                passList.add(endpoint);

                tMapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, curPosition, endpoint2, passList,
                        0, new TMapData.FindPathDataListenerCallback() {
                            @Override
                            public void onFindPathData(TMapPolyLine tMapPolyLine) {
                                tMapPolyLine.setLineColor(Color.BLUE);
                                tMapView.addTMapPath(tMapPolyLine);
                            }
                        });

            } else if (Index == 1) { //가게1 > 가게2 > 목적지1 > 목적지2
                ArrayList<TMapPoint> passList = new ArrayList<>();
                passList.add(startpoint);
                passList.add(endpoint);
                passList.add(startpoint2);

                tMapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, curPosition, endpoint2, passList,
                        0, new TMapData.FindPathDataListenerCallback() {
                            @Override
                            public void onFindPathData(TMapPolyLine tMapPolyLine) {
                                tMapPolyLine.setLineColor(Color.BLUE);
                                tMapView.addTMapPath(tMapPolyLine);
                            }
                        });

            } else if (Index == 2) { //가게1 > 가게2 > 목적지2 > 목적지1
                ArrayList<TMapPoint> passList = new ArrayList<>();
                passList.add(startpoint);
                passList.add(endpoint);
                passList.add(endpoint2);

                tMapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, curPosition, startpoint2, passList,
                        0, new TMapData.FindPathDataListenerCallback() {
                            @Override
                            public void onFindPathData(TMapPolyLine tMapPolyLine) {
                                tMapPolyLine.setLineColor(Color.BLUE);
                                tMapView.addTMapPath(tMapPolyLine);
                            }
                        });

            } else if (Index == 3) { //가게2 > 가게1 > 목적지1 > 목적지2
                ArrayList<TMapPoint> passList = new ArrayList<>();
                passList.add(endpoint);
                passList.add(startpoint);
                passList.add(startpoint2);

                tMapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, curPosition, endpoint2, passList,
                        0, new TMapData.FindPathDataListenerCallback() {
                            @Override
                            public void onFindPathData(TMapPolyLine tMapPolyLine) {
                                tMapPolyLine.setLineColor(Color.BLUE);
                                tMapView.addTMapPath(tMapPolyLine);
                            }
                        });

            } else if (Index == 4) { //가게2 > 가게1 > 목적지2 > 목적지1
                ArrayList<TMapPoint> passList = new ArrayList<>();
                passList.add(endpoint);
                passList.add(startpoint);
                passList.add(endpoint2);

                tMapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, curPosition, startpoint2, passList,
                        0, new TMapData.FindPathDataListenerCallback() {
                            @Override
                            public void onFindPathData(TMapPolyLine tMapPolyLine) {
                                tMapPolyLine.setLineColor(Color.BLUE);
                                tMapView.addTMapPath(tMapPolyLine);
                            }
                        });

            } else { //가게2 > 목적지2 > 가게1 > 목적지1
                ArrayList<TMapPoint> passList = new ArrayList<>();
                passList.add(endpoint);
                passList.add(endpoint2);
                passList.add(startpoint);

                tMapData.findPathDataWithType(TMapData.TMapPathType.PEDESTRIAN_PATH, curPosition, startpoint2, passList,
                        0, new TMapData.FindPathDataListenerCallback() {
                            @Override
                            public void onFindPathData(TMapPolyLine tMapPolyLine) {
                                tMapPolyLine.setLineColor(Color.BLUE);
                                tMapView.addTMapPath(tMapPolyLine);
                            }
                        });
            }
        }
    }
    // 자동차 경로 찾는 함수
    public void FindPath_Car(int Limit){
        curPosition = new TMapPoint(tMapGpsManager.getLocation().getLatitude(), tMapGpsManager.getLocation().getLongitude());
        PathPlusFood(Limit);
        if(endMarker.getName().length()<=2){ //가게 목적지 하나씩 일 경우
            TMapPoint startpoint = new TMapPoint(startMarker.getLatitude(),startMarker.getLongitude());
            TMapPoint startpoint2 = new TMapPoint(startMarker2.getLatitude(),startMarker2.getLongitude());
            ArrayList<TMapPoint> passList = new ArrayList<>();
            passList.add(startpoint);

            tMapData.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, curPosition, startpoint2,  passList,
                    0, new TMapData.FindPathDataListenerCallback() {
                        @Override
                        public void onFindPathData(TMapPolyLine tMapPolyLine) {
                            tMapPolyLine.setLineColor(Color.BLUE);
                            tMapView.addTMapPath(tMapPolyLine);
                        }
                    });
        }else {
            TMapPoint startpoint = new TMapPoint(startMarker.getLatitude(), startMarker.getLongitude());
            TMapPoint endpoint = new TMapPoint(endMarker.getLatitude(), endMarker.getLongitude());
            TMapPoint startpoint2 = new TMapPoint(startMarker2.getLatitude(), startMarker2.getLongitude());
            TMapPoint endpoint2 = new TMapPoint(endMarker2.getLatitude(), endMarker2.getLongitude());

            if (Index == 0) { //가게1 > 목적지1 > 가게2 > 목적지2
                ArrayList<TMapPoint> passList = new ArrayList<>();
                passList.add(startpoint);
                passList.add(startpoint2);
                passList.add(endpoint);

                tMapData.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, curPosition, endpoint2, passList,
                        0, new TMapData.FindPathDataListenerCallback() {
                            @Override
                            public void onFindPathData(TMapPolyLine tMapPolyLine) {
                                tMapPolyLine.setLineColor(Color.BLUE);
                                tMapView.addTMapPath(tMapPolyLine);
                            }
                        });

            } else if (Index == 1) { //가게1 > 가게2 > 목적지1 > 목적지2
                ArrayList<TMapPoint> passList = new ArrayList<>();
                passList.add(startpoint);
                passList.add(endpoint);
                passList.add(startpoint2);

                tMapData.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, curPosition, endpoint2, passList,
                        0, new TMapData.FindPathDataListenerCallback() {
                            @Override
                            public void onFindPathData(TMapPolyLine tMapPolyLine) {
                                tMapPolyLine.setLineColor(Color.BLUE);
                                tMapView.addTMapPath(tMapPolyLine);
                            }
                        });

            } else if (Index == 2) { //가게1 > 가게2 > 목적지2 > 목적지1
                ArrayList<TMapPoint> passList = new ArrayList<>();
                passList.add(startpoint);
                passList.add(endpoint);
                passList.add(endpoint2);

                tMapData.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, curPosition, startpoint2, passList,
                        0, new TMapData.FindPathDataListenerCallback() {
                            @Override
                            public void onFindPathData(TMapPolyLine tMapPolyLine) {
                                tMapPolyLine.setLineColor(Color.BLUE);
                                tMapView.addTMapPath(tMapPolyLine);
                            }
                        });

            } else if (Index == 3) { //가게2 > 가게1 > 목적지1 > 목적지2
                ArrayList<TMapPoint> passList = new ArrayList<>();
                passList.add(endpoint);
                passList.add(startpoint);
                passList.add(startpoint2);

                tMapData.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, curPosition, endpoint2, passList,
                        0, new TMapData.FindPathDataListenerCallback() {
                            @Override
                            public void onFindPathData(TMapPolyLine tMapPolyLine) {
                                tMapPolyLine.setLineColor(Color.BLUE);
                                tMapView.addTMapPath(tMapPolyLine);
                            }
                        });

            } else if (Index == 4) { //가게2 > 가게1 > 목적지2 > 목적지1
                ArrayList<TMapPoint> passList = new ArrayList<>();
                passList.add(endpoint);
                passList.add(startpoint);
                passList.add(endpoint2);

                tMapData.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, curPosition, startpoint2, passList,
                        0, new TMapData.FindPathDataListenerCallback() {
                            @Override
                            public void onFindPathData(TMapPolyLine tMapPolyLine) {
                                tMapPolyLine.setLineColor(Color.BLUE);
                                tMapView.addTMapPath(tMapPolyLine);
                            }
                        });

            } else { //가게2 > 목적지2 > 가게1 > 목적지1
                ArrayList<TMapPoint> passList = new ArrayList<>();
                passList.add(endpoint);
                passList.add(endpoint2);
                passList.add(startpoint);

                tMapData.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, curPosition, startpoint2, passList,
                        0, new TMapData.FindPathDataListenerCallback() {
                            @Override
                            public void onFindPathData(TMapPolyLine tMapPolyLine) {
                                tMapPolyLine.setLineColor(Color.BLUE);
                                tMapView.addTMapPath(tMapPolyLine);
                            }
                        });
            }
        }
    }

    //이동수단 버튼 클릭 이벤트 관련 함수
    public void SelectTransPort(int value){
        if(findPath_case == value) return;
        switch (findPath_case){
            case 0:
                select_car_btn.setSelected(false);
                break;

            case 1:
                select_pedestrian_btn.setSelected(false);
                break;
        }
        findPath_case = value;
    }


    @Override
    public void onLocationChange(Location location) {
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());

        TMapPoint tMapPoint1 = new TMapPoint(location.getLatitude(), location.getLongitude());
        TMapMarkerItem NowLocat = new TMapMarkerItem();

        Bitmap bitmapIcon = createMarkerIcon(R.drawable.curposition);
        NowLocat.setIcon(bitmapIcon);
        NowLocat.setTMapPoint(tMapPoint1); // 마커의 좌표 지정
        NowLocat.setName("내위치"); // 마커의 타이틀 지정
        tMapView.addMarkerItem("markerItem", NowLocat);
    }

    private Bitmap createMarkerIcon(int image)
    {
        //아이콘 디자인 설정 메서드
        Log.e("MapViewActivity", "(F)   createMarkerIcon()");

        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), image);
        bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100,false);

        return bitmap;
    }
    @Override
    public void onBackPressed() {
        finish();
    }

}