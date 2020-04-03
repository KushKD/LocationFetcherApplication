package com.doi.test;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivityWithAsyncTask extends AppCompatActivity {

    EditText latitudeEdit, longitudeEdit, addressEdit;
    ProgressBar progressBar;
    TextView infoText;
    CheckBox checkBox;
    Button button;
    List<POJO> mou_details;

    ProgressDialog dialog;
    List<MainActivityWithAsyncTask.create_db_mouDetails> tasks = new ArrayList<>();

    public static final int USE_ADDRESS_NAME = 1;
    public static final int USE_ADDRESS_LOCATION = 2;

    int fetchType = USE_ADDRESS_LOCATION;

    private static final String TAG = "MAIN_ACTIVITY_ASYNC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //longitudeEdit = (EditText) findViewById(R.id.longitudeEdit);
        //latitudeEdit = (EditText) findViewById(R.id.latitudeEdit);
        addressEdit = (EditText) findViewById(R.id.addressEdit);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        infoText = (TextView) findViewById(R.id.infoText);
        button = (Button)findViewById(R.id.button);
//        checkBox = (CheckBox) findViewById(R.id.checkbox);

        DatabaseHandler DB = new DatabaseHandler(MainActivityWithAsyncTask.this);
        if(DB.getNoOfRowsCount()==0){
            Log.e("Total Rows", Integer.toString(DB.getNoOfRowsCount()));
            MainActivityWithAsyncTask.create_db_mouDetails mouDetails_task = new MainActivityWithAsyncTask.create_db_mouDetails();
            mouDetails_task.execute();

        }else{
            Log.e("Total Rows", Integer.toString(DB.getNoOfRowsCount()));
            mou_details = DB.GetAllData();

         new GeocodeAsyncTask().execute(mou_details);
        }




    }



    class create_db_mouDetails extends AsyncTask<String,String,String> {




        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = ProgressDialog.show(MainActivityWithAsyncTask.this, "Loading", "Updating Database .. Please Wait", true);
            dialog.setCancelable(false);

            if (tasks.size() == 0) {
                // pb.setVisibility(View.VISIBLE);
            }
            tasks.add(this);

        }

        @Override
        protected String doInBackground(String... params) {

            //Read file From Assets

            JSONArray m_jArry = null;
            try {
                m_jArry = new JSONArray(loadJSONFromAsset());
                Log.e("Result",m_jArry.toString());
                mou_details  = new ArrayList<>();

                for (int i = 0; i < m_jArry.length(); i++) {
                    // Log.e("Array",m_jArry.toString());
                    POJO am = new POJO();
                    JSONObject jo_inside = m_jArry.getJSONObject(i);
                    am.setShopname(jo_inside.getString("Shopname"));
                    am.setOwnername(jo_inside.getString("Ownername"));
                    am.setAddress(jo_inside.getString("Address"));
                    am.setMobile(jo_inside.getString("Mobile"));
                    am.setItems(jo_inside.getString("Items"));
                    am.setDistrict(jo_inside.getString("District"));
                     am.setTehsil(jo_inside.getString("Tehsil"));
                    am.setPincode(jo_inside.getString("pincode"));




                    //Add your values in your `ArrayList` as below:
                    // Log.e(Integer.toString(i),am.toString());
                    mou_details.add(am);


                }
                if(mou_details.isEmpty()){
                    Toast.makeText(getApplicationContext(),"No Record Found",Toast.LENGTH_LONG).show();
                }else
                {
                    //Store Data to Database  //mou_details
                    DatabaseHandler DB = new DatabaseHandler(MainActivityWithAsyncTask.this);
                    DB.addMouDetails(mou_details);




                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

            return m_jArry.toString();


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            tasks.remove(this);

            if (tasks.size() == 0) {
                // pb.setVisibility(View.GONE);
            }
            dialog.dismiss();
        }
    }






    class GeocodeAsyncTask extends AsyncTask<List<POJO>, Void, Address> {

        String errorMessage = "";



        @Override
        protected void onPreExecute() {
            infoText.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Address doInBackground(List<POJO>... params) {
            Geocoder geocoder = new Geocoder(MainActivityWithAsyncTask.this, Locale.getDefault());
            List<Address> addresses = null;
            List<POJO> data = params[0];
            DatabaseHandler DB = new DatabaseHandler(MainActivityWithAsyncTask.this);
            for (int i=0; i<data.size();i++){

                if(!data.get(i).getPincode().isEmpty() && data.get(i).getPincode().length()==6){
                    //Do Processing
                    try {
                       // String address = address,district,tehsil,state,country,pin
                        String address = data.get(i).getAddress().trim() +","+
                                          data.get(i).getTehsil().trim()+","+
                                data.get(i).getDistrict().trim()+","+
                                "Haryana,"+"India,"+data.get(i).getPincode().trim();
                        System.out.println(i+" Address:-" + address);

                        addresses = geocoder.getFromLocationName(address, 1);

                        if(addresses != null && addresses.size() > 0){
                            data.get(i).setLatitude(String.valueOf(addresses.get(0).getLatitude()));
                            data.get(i).setLongitude(String.valueOf(addresses.get(0).getLongitude()));
                            data.get(i).setAddress_updated(address);
                            //Update Object
                            DB.updateData( data.get(i));
                           // Log.e(i+" Address:- " , address+" "+ data.toString());
                            //return addresses.get(0);
                        }else{
                            data.get(i).setLatitude("-");
                            data.get(i).setLongitude("-");
                            data.get(i).setAddress_updated(address);
                            //Update Object
                            DB.updateData( data.get(i));
                            //Log.e(i+" Address:- " , address+" "+ data.toString());

                            //return null;
                        }

                    } catch (IOException e) {
                        errorMessage = "Service not available";
                        Log.e(TAG, errorMessage, e);
                    }
                }else{
                    data.get(i).setLongitude("-");
                    data.get(i).setLatitude("-");
                }
            }



            return null;
        }

        protected void onPostExecute(Address address) {
            if(address == null) {
                progressBar.setVisibility(View.INVISIBLE);
                infoText.setVisibility(View.VISIBLE);
                infoText.setText(errorMessage);
            }
            else {
                String addressName = "";
                for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    addressName += " --- " + address.getAddressLine(i);
                }
                progressBar.setVisibility(View.INVISIBLE);
                infoText.setVisibility(View.VISIBLE);
                infoText.setText("Latitude: " + address.getLatitude() + "\n" +
                        "Longitude: " + address.getLongitude() + "\n" +
                        "Address: " + addressName);
            }
        }


    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = MainActivityWithAsyncTask.this.getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


}
