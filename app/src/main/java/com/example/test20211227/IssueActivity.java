package com.example.test20211227;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IssueActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Adapter_Notice adapterNotice;
    ArrayList<Notice> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue_activity);

        list = (ArrayList<Notice>) getIntent().getSerializableExtra("notice");
        recyclerView = (RecyclerView)findViewById(R.id.recyceler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapterNotice = new Adapter_Notice();
        for (int i = 0; i < list.size(); i++) {
            adapterNotice.setArrayData(list.get(i));
        }
        recyclerView.setAdapter(adapterNotice);
    }

}


