package com.example.test20211227;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import java.sql.Connection;


public class Fragment3 extends Fragment {

    private ImageButton imgButton;
    private Switch switchbtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_3,container,false);

        switchbtn = rootView.findViewById(R.id.switch_btn);

        switchbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    imgButton = rootView.findViewById(R.id.imageButton1);
                    imgButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), StorePedestrian.class);
                            startActivity(intent);
                        }
                    });

                    imgButton = rootView.findViewById(R.id.imageButton2);
                    imgButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), GasPedestrian.class);
                            startActivity(intent);

                        }
                    });

                    imgButton = rootView.findViewById(R.id.imageButton3);
                    imgButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), ToiletPedestrian.class);
                            startActivity(intent);

                        }
                    });

                }else{
                    imgButton = rootView.findViewById(R.id.imageButton1);
                    imgButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), StoreCar.class);
                            startActivity(intent);

                        }
                    });

                    imgButton = rootView.findViewById(R.id.imageButton2);
                    imgButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), GasCar.class);
                            startActivity(intent);

                        }
                    });

                    imgButton = rootView.findViewById(R.id.imageButton3);
                    imgButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), ToiletCar.class);
                            startActivity(intent);

                        }
                    });

                }
            }
        });
        imgButton = rootView.findViewById(R.id.imageButton1);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StoreCar.class);
                startActivity(intent);

            }
        });

        imgButton = rootView.findViewById(R.id.imageButton2);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GasCar.class);
                startActivity(intent);

            }
        });

        imgButton = rootView.findViewById(R.id.imageButton3);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ToiletCar.class);
                startActivity(intent);

            }
        });
        return rootView;
    }
}