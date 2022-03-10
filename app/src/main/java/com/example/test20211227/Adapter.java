package com.example.test20211227;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class Adapter extends RecyclerView.Adapter<ViewHolder> {

private ArrayList<Notice> arrayList;

        public Adapter() {
                arrayList = new ArrayList<>();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Context context = parent.getContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.listitem, parent, false);

                ViewHolder viewholder = new ViewHolder(context, view);

                return viewholder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                if (arrayList.isEmpty()){ holder.textView.setText("공지 사항이 없습니다."); return;}
                Notice text = arrayList.get(position);
                holder.textView.setText(text.getAcc_info());
                holder.textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                Intent intent = new Intent(v.getContext(),PopupActivity.class);
                                intent.putExtra("popup",text);
                                v.getContext().startActivity(intent);

                        }
                });
        }

        @Override
        public int getItemCount() {
        return arrayList.size();
        }

        // 데이터를 입력
        public void setArrayData(Notice strData) {
                arrayList.add(strData);
        }



}
