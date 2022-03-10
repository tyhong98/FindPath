package com.example.test20211227;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.TMapPOIItem;
import java.util.ArrayList;


public class StoreCar extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback{
    private TMapView tMapView;
    TMapGpsManager tMapGPS = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storecar_activity);

        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("l7xx56f6c527fe08492f94c27fa47f861229");

        LinearLayout mLlMapView = (LinearLayout)findViewById(R.id.linearLayoutTmap);
        mLlMapView.addView(tMapView);
        tMapView.setZoomLevel(17);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        tMapGPS = new TMapGpsManager(this);
        tMapGPS.setProvider(tMapGPS.NETWORK_PROVIDER);
        tMapGPS.OpenGps();

        tMapView.setOnCalloutRightButtonClickListener(mOnCalloutRightButtonClickCallback);
    }
    @Override
    public void onLocationChange(Location location) {
        tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        tMapView.setCenterPoint(location.getLongitude(), location.getLatitude());

        TMapPoint tMapPoint1 = new TMapPoint(location.getLatitude(), location.getLongitude());
        TMapMarkerItem NowLocat = new TMapMarkerItem();
        Bitmap bitmapIcon = createMarkerIcon(R.drawable.curposition);

        NowLocat.setIcon(bitmapIcon);
        NowLocat.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        NowLocat.setTMapPoint(tMapPoint1); // 마커의 좌표 지정
        NowLocat.setName("내위치"); // 마커의 타이틀 지정
        tMapView.addMarkerItem("markerItem", NowLocat);

        final TMapData tmapdata = new TMapData();
        final ArrayList<TMapPoint> arrTMapPoint = new ArrayList<>();
        final ArrayList<String> arrTitle = new ArrayList<>();
        final ArrayList<String> arrAddress = new ArrayList<>();
        tmapdata.findAroundNamePOI(tMapPoint1, "편의점", 3, 99,
                new TMapData.FindAroundNamePOIListenerCallback() {
                    @Override
                    public void onFindAroundNamePOI(ArrayList<TMapPOIItem> poiItem) {
                        for (int i = 0; i < poiItem.size(); i++) {
                            TMapPOIItem tMapPOIItem = poiItem.get(i);
                            arrTMapPoint.add(tMapPOIItem.getPOIPoint());
                            arrTitle.add(tMapPOIItem.getPOIName());
                            arrAddress.add(tMapPOIItem.upperAddrName + " " +
                                    tMapPOIItem.middleAddrName + " " + tMapPOIItem.lowerAddrName);
                        }
                        //다중 Marker 생성 메서드
                        for (int i = 0; i < arrTMapPoint.size(); i++) {
                            Bitmap bitmapIcon = createMarkerIcon(R.drawable.poi_star);

                            TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();
                            tMapMarkerItem.setIcon(bitmapIcon);

                            tMapMarkerItem.setTMapPoint(arrTMapPoint.get(i));
                            tMapView.addMarkerItem("markerItem" + i, tMapMarkerItem);

                            //풍선뷰 설정 메서드
                            tMapMarkerItem.setCanShowCallout(true);

                            if (tMapMarkerItem.getCanShowCallout()) {
                                tMapMarkerItem.setCalloutTitle(arrTitle.get(i));
                                tMapMarkerItem.setCalloutSubTitle(arrAddress.get(i));

                                Bitmap bitmap = createMarkerIcon(R.drawable.clustering);
                                tMapMarkerItem.setCalloutRightButtonImage(bitmap);
                            }
                        }
                    }
                });
    }

    private Bitmap createMarkerIcon(int image)
    {
        //아이콘 디자인 설정 메서드
        Log.e("MapViewActivity", "(F)   createMarkerIcon()");

        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), image);
        bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100,false);

        return bitmap;
    }
    private int nRightButtonCount = 0;
    TMapView.OnCalloutRightButtonClickCallback mOnCalloutRightButtonClickCallback = new TMapView.OnCalloutRightButtonClickCallback() {
        //마커 오른쪽 버튼 클릭시
        @Override
        public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
            TMapGpsManager gps = tMapGPS;
            TMapPoint tMapPoint = tMapMarkerItem.getTMapPoint();

            if (nRightButtonCount == 0)//자동차 기준 길찾기
            {
                //좌표로 길 생성
                TMapPoint tMapPointStart = new TMapPoint(gps.getLocation().getLatitude(), gps.getLocation().getLongitude());

                FindCarPathTask findCarPathTask = new FindCarPathTask(getApplicationContext(),
                        tMapView);
                findCarPathTask.execute(tMapPointStart, tMapPoint);

                tMapView.setCenterPoint(gps.getLocation().getLongitude(), gps.getLocation().getLatitude());
            } else if (nRightButtonCount == 1)//보행자 기준 길찾기
            {
                //좌표로 길 생성
                TMapPoint tMapPointStart = new TMapPoint(gps.getLocation().getLatitude(), gps.getLocation().getLongitude());

                FindPedestrianPathTask findpedestrianPathTask = new FindPedestrianPathTask(getApplicationContext(),
                        tMapView);
                findpedestrianPathTask.execute(tMapPointStart, tMapPoint);

                tMapView.setCenterPoint(gps.getLocation().getLongitude(), gps.getLocation().getLatitude());
            }
        }
    };
}