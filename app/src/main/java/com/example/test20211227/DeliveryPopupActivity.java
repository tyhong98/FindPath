package com.example.test20211227;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DeliveryPopupActivity extends Activity {
    TextView txtText1;
    TextView txtText2;
    TextView txtText3;
    TextView txtText4;
    TextView txtText5;
    TextView txtText6;
    Delivery delivery;
    TextView deliveryCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showdelivery);

        delivery = (Delivery) getIntent().getSerializableExtra("deliveryInfo");

        txtText1 = (TextView) findViewById(R.id.deliveryFood);
        txtText2 = (TextView) findViewById(R.id.deliveryDate);
        txtText3 = (TextView) findViewById(R.id.restaurantAddress);
        txtText4 = (TextView) findViewById(R.id.deliveryAddress);
        txtText5 = (TextView) findViewById(R.id.deliveryMeans);
        txtText6 = (TextView) findViewById(R.id.deliveryEtc);
        deliveryCancel = (TextView) findViewById(R.id.deliveryCancel);

        txtText1.setText(delivery.getFoodName());
        txtText2.setText(delivery.getDate());
        txtText3.setText(delivery.getRestaurantAddress());
        txtText4.setText(delivery.getDeliveryAddress());
        txtText5.setText(delivery.getMeans());
        txtText6.setText(delivery.getEtc());

        deliveryCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
