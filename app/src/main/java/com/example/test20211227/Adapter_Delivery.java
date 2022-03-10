package com.example.test20211227;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Delivery extends RecyclerView.Adapter<ViewHolder> {
    private ArrayList<Delivery> arrayList;

    public Adapter_Delivery() {
        arrayList = new ArrayList<>();
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.listitem, parent, false);

        ViewHolder viewHolder = new ViewHolder(context, view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if(arrayList.get(position).getDeliveryAddress().equals("2")){ holder.textView.setText("배달 내역이 없습니다."); return;}
            Delivery text = arrayList.get(position);
            holder.textView.setText(text.getRestaurantAddress() + " -> " + text.getDeliveryAddress());
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DeliveryPopupActivity.class);
                    intent.putExtra("deliveryInfo", text);
                    v.getContext().startActivity(intent);
                }
            });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    // 데이터를 입력
    public void setArrayData(Delivery strData) {
        arrayList.add(strData);
    }



}
