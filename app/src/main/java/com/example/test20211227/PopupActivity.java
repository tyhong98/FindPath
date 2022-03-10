package com.example.test20211227;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class PopupActivity extends Activity {
    TextView txtText1;
    TextView txtText2;
    TextView txtText3;
    TextView txtText4;
    TextView cancel;
    Notice notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shownotice);

        txtText1 = (TextView) findViewById(R.id.issue_ID);
        txtText2 = (TextView) findViewById(R.id.OccrTime);
        txtText3 = (TextView) findViewById(R.id.ExpClTime);
        txtText4 = (TextView) findViewById(R.id.AccInfo);
        cancel = (TextView) findViewById(R.id.option_codetype_dialog_negative);

        notice = (Notice) getIntent().getSerializableExtra("popup");

        txtText1.setText(String.valueOf(notice.getAcc_id()));
        txtText2.setText(notice.getOccr_date().substring(0,4)+"년 " + notice.getOccr_date().substring(5,7) + "월 " +
                notice.getOccr_date().substring(8,10) + "일 " + notice.getOccr_time().substring(0,2)+"시 " + notice.getOccr_time().substring(3,5) + "분");
        txtText3.setText(notice.getExp_clr_date().substring(0,4)+"년 " + notice.getExp_clr_date().substring(5,7) + "월 " +
                notice.getExp_clr_date().substring(8,10) + "일 " + notice.getExp_clr_time().substring(0,2)+"시 " + notice.getExp_clr_time().substring(3,5) + "분");
        txtText4.setText(notice.getAcc_info());


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
