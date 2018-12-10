package com.mobileapps.theweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText etCityName;
    TextView tvTheResult;
    String  cityToFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCityName=findViewById(R.id.etCityName);
        tvTheResult=findViewById(R.id.tvTextView2);


    }

    public void FindWeather (View v){
        cityToFind= etCityName.getText().toString();

        try{

            ExecuteTask task= new ExecuteTask();
            task.execute("http://api.openweathermap.org/data/2.5/weather?q="+cityToFind+"&APPID=c2d27bae28c07293bd4e95217f97ec8e");



        }catch (Exception e){

            e.printStackTrace();
        }



    }



    public class ExecuteTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(strings[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream is = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(is);

                int data = reader.read();
                while (data != -1){
                    char current = (char) data;

                    result += current;

                    data = reader.read();
                }

                return result;



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try{
                String message = "";
                JSONObject jsonObject=new JSONObject(s);

                String infoWeatherToday = jsonObject.getString("weather");

                JSONArray array = new JSONArray(infoWeatherToday);

                for (int i=0; i<array.length(); i++){
                    JSONObject jsonSecondary = array.getJSONObject(i);

                    String main = "";
                    String description= "";

                    main = jsonSecondary.getString("main");
                    description = jsonSecondary.getString("description");

                    if (main !="" && description != ""){
                        message += main + "\n "+description+"\n";
                    }
                }

                if (message!=""){
                    tvTheResult.setText(message);

                }

                else {
                    Toast.makeText(MainActivity.this,"An Error Occurred",Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



}
