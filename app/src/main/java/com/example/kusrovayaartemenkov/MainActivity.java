package com.example.kusrovayaartemenkov;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText userFiled;
    private Button mainButton;
    private TextView infoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userFiled = (EditText) findViewById(R.id.user_field);
        mainButton = (Button) findViewById(R.id.mainButton);
        infoText = (TextView) findViewById(R.id.result);

        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userFiled.getText().toString().trim().equals("")){
                    Toast.makeText(MainActivity.this,R.string.noInfo,Toast.LENGTH_LONG).show();
                }
                else{
                    String city = userFiled.getText().toString();
                    String key = "838a85d9a5a09b68f34b66f45075130f";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+key+"&lang=ru&units=metric";

                    new URLData().execute(url);
                }
            }
        });
    }

    private class URLData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            infoText.setText("Ждите результата");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();
                String line = "";

                while((line = reader.readLine()) != null){
                    stringBuffer.append(line).append("\n");
                }

                return stringBuffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }
                try{
                    if (reader != null)
                        reader.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override protected void onPostExecute(String result){
            super.onPostExecute(result);

            try {
                JSONObject object=new JSONObject(result);
                infoText.setText("Температура: " + object.getJSONObject("main").getDouble("temp"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}