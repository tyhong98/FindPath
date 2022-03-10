package com.example.test20211227;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class ViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;

    ViewHolder(Context context, View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.issueText);
    }
}

