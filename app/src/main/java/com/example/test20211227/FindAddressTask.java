package com.example.test20211227;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class FindAddressTask extends AsyncTask<String, Void, String>
{
    Context context;
    String[] arrParametersName = new String[4];
    String[] arrJsonKeys = new String[5];

    public FindAddressTask(Context context)
    {
        this.context = context;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }


    @Override
    protected void onPostExecute(String totalAddress)
    {
        super.onPreExecute();

        Toast.makeText(context, "주소 : " + totalAddress + "", Toast.LENGTH_LONG).show();
    }

    @Override
    protected String doInBackground(String[] args)
    {
        setArrays();

        TMapWebService tMapWebService = new TMapWebService("https://api2.sktelecom.com/tmap/"
                + "geo/reversegeocoding");
        tMapWebService.setParameters(arrParametersName, args, 4);
        String totalAddress = tMapWebService.connectWebService(arrJsonKeys);

        return totalAddress;
    }

    private void setArrays()
    {
        arrParametersName[0] = "version";
        arrParametersName[1] = "appKey";
        arrParametersName[2] = "lat";
        arrParametersName[3] = "lon";

        arrJsonKeys[0] = "addressInfo";
        arrJsonKeys[1] = "fullAddress";
        arrJsonKeys[2] = "roadName";
        arrJsonKeys[3] = "buildingIndex";
        arrJsonKeys[4] = "buildingName";
    }
}

