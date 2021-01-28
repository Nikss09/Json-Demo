package com.example.jsondemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView text;

    public class downloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpURLConnection urlConnection=null;

            try {
                url=new URL(urls[0]);
                urlConnection= (HttpURLConnection) url.openConnection();

                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);

                int data=reader.read();
                while(data!=-1){
                    char current=(char)data;
                    result+=current;
                    data=reader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject=new JSONObject(s);
                String weather_info=jsonObject.getString("weather");
                Log.i("Weather is......",weather_info);

                JSONArray jsonArray=new JSONArray(weather_info);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonPart=jsonArray.getJSONObject(i);
                    Log.i("Main",""+jsonPart.getString("main"));
                    Log.i("Description",""+jsonPart.getString("description"));
                    text.setText("Main: "+jsonPart.getString("main")+"\nDescription: "+jsonPart.getString("description"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downloadTask task=new downloadTask();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q=Jaipur&appid=61f5c5fba2da9469007a69bd69ae2218");
        text=findViewById(R.id.textView2);
    }
}