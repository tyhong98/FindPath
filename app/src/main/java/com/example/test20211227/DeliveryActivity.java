package com.example.test20211227;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DeliveryActivity extends Activity {
    RecyclerView recyclerView;
    Adapter_Delivery adapterDelivery;
    ArrayList<Delivery> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_activity);

        list = (ArrayList<Delivery>) getIntent().getSerializableExtra("DeliveryList");
        recyclerView = (RecyclerView)findViewById(R.id.delivery_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapterDelivery = new Adapter_Delivery();
        for (int i = 0; i < list.size(); i++) {
            adapterDelivery.setArrayData(list.get(i));
        }
        recyclerView.setAdapter(adapterDelivery);
    }

}
