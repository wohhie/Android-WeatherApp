package com.example.wohhi.weatherforcast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private LinearLayout layout;
    private TextView temperatureTV;
    private TextView locationTV;
    private TextView conditionTV;
    private TextView todayTV;
    private TextView tempDay1TV;
    private TextView tempDay2TV;
    private TextView tempDay3TV;
    private TextView dateDay1TV;
    private TextView dateDay2TV;
    private TextView dateDay3TV;
    private TextView sunriseTV;
    private TextView sunsetTV;
    private TextView maxTempTV;
    private TextView minTempTV;


    private ToggleButton toggleButton;
    private ImageView conditionImage2;
    private GPSTracker gpsTracker;

    private int temp,
                day1Min,
                day1Max,
                day2Min,
                day2Max,
                day3Min,
                day3Max,
                max,
                min;
    private String openWeatherURL = "http://api.openweathermap.org/data/2.5/weather?lat=23.789564&lon=90.391317&appid=09711c134d49696e2b4724ab9407fb49";
    private String yahooWeahterURL = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22Dhaka%2C%20bd%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize
        initialize();
        showService();

        Toast.makeText(MainActivity.this, "Lat: " + gpsTracker.getLantitude() +"\nLon: " + gpsTracker.getLongtitude(), Toast.LENGTH_SHORT).show();;

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTemp();
            }
        });


    }

    private void initialize(){

        layout = (LinearLayout) findViewById(R.id.activity_main);

        temperatureTV = (TextView) findViewById(R.id.tempTV);
        temperatureTV.setTypeface(FontClass.getLightFont(this));

        locationTV = (TextView) findViewById(R.id.locationTV);
        locationTV.setTypeface(FontClass.getLightFont(this));

        todayTV = (TextView) findViewById(R.id.todayTV);
        todayTV.setTypeface(FontClass.getLightFont(this));


        tempDay1TV = (TextView) findViewById(R.id.tempDay1);
        tempDay2TV = (TextView) findViewById(R.id.tempDay2);
        tempDay3TV = (TextView) findViewById(R.id.tempDay3);

        dateDay1TV = (TextView) findViewById(R.id.dateDay1);
        dateDay2TV = (TextView) findViewById(R.id.dateDay2);
        dateDay3TV = (TextView) findViewById(R.id.dateDay3);

        sunriseTV = (TextView) findViewById(R.id.sunriseTV);
        sunsetTV = (TextView) findViewById(R.id.sunsetTV);

        maxTempTV = (TextView) findViewById(R.id.maxTempTV);
        minTempTV = (TextView) findViewById(R.id.minTempTV);

        conditionTV = (TextView) findViewById(R.id.condition);
        conditionTV.setTypeface(FontClass.getLightFont(this));

        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        gpsTracker = new GPSTracker(this);

    }

    public void showService(){

        JsonObjectRequest openWeatherObjectRequest = new JsonObjectRequest(Request.Method.GET, openWeatherURL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response != null){
                    try {
                        JSONArray weatherJsonArray = new JSONArray("weather");
                        Picasso.with(MainActivity.this)
                                .load("http://openweahtermap.org/img/w/" + weatherJsonArray.getJSONObject(0).getString("icon") + ".png")
                                .into(conditionImage2);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });



        JsonObjectRequest yahooWeahterObjectReqest = new JsonObjectRequest(Request.Method.GET, yahooWeahterURL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response != null){

                    try {
                        JSONObject queryObject = response.getJSONObject("query");
                        JSONObject resultObject = queryObject.getJSONObject("results");
                        JSONObject channelObject = resultObject.getJSONObject("channel");
                        JSONObject astronmyObject = channelObject.getJSONObject("astronomy");
                        JSONObject locationObject = channelObject.getJSONObject("location");
                        JSONObject item = channelObject.getJSONObject("item");
                        JSONObject conditionObject = item.getJSONObject("condition");


                        JSONArray forecastArray = item.getJSONArray("forecast");
                        ArrayList<Forecast> forecasts = new ArrayList<>();

                        for (int i = 0; i < forecastArray.length(); i++){
                            Forecast forecast = new Forecast();
                            forecast.setCode(forecastArray.getJSONObject(i).getString("code"));
                            forecast.setDate(forecastArray.getJSONObject(i).getString("date"));
                            forecast.setDay(forecastArray.getJSONObject(i).getString("day"));
                            forecast.setHigh(forecastArray.getJSONObject(i).getString("high"));
                            forecast.setLow(forecastArray.getJSONObject(i).getString("low"));
                            forecast.setText(forecastArray.getJSONObject(i).getString("text"));
                            forecasts.add(forecast);
                        }


                        String temperature = conditionObject.getString("temp");
                        String city = locationObject.getString("city");
                        String country = locationObject.getString("country");
                        String condition = conditionObject.getString("text");
                        String date = conditionObject.getString("date");
                        String code = conditionObject.getString("code");


                        temp = Integer.parseInt(temperature);
                        max = Integer.parseInt(forecasts.get(0).getHigh());
                        min = Integer.parseInt(forecasts.get(0).getLow());

                        day1Min = Integer.parseInt(forecasts.get(1).getLow());
                        day1Max = Integer.parseInt(forecasts.get(1).getHigh());

                        day2Min = Integer.parseInt(forecasts.get(2).getLow());
                        day2Max = Integer.parseInt(forecasts.get(2).getHigh());

                        day3Min = Integer.parseInt(forecasts.get(3).getLow());
                        day3Max = Integer.parseInt(forecasts.get(3).getHigh());

                        setTemp();

                        switch (condition){
                            case "Clear":
                                layout.setBackgroundResource(R.drawable.clear);
                                break;
                            case "Rain":
                                layout.setBackgroundResource(R.drawable.rain);
                                break;
                            case "Thunderstorms":
                                layout.setBackgroundResource(R.drawable.thunderstorm);

                            default:
                                layout.setBackgroundResource(R.drawable.bg);

                        }

                        dateDay1TV.setText("Tomorrow");
                        dateDay2TV.setText(forecasts.get(2).getDay());
                        dateDay3TV.setText(forecasts.get(3).getDay());


                        sunriseTV.setText(astronmyObject.getString("sunrise"));
                        sunsetTV.setText(astronmyObject.getString("sunset"));
                        locationTV.setText(city + ", " + country);
                        todayTV.setText(date);

                        conditionTV.setText(condition);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something error in yahoo", Toast.LENGTH_SHORT).show();
            }
        });


        AppController.getInstance().addToRequestQueue(yahooWeahterObjectReqest);
        AppController.getInstance().addToRequestQueue(openWeatherObjectRequest);
    }

    private void setTemp() {
        if(toggleButton.isChecked()){
            temperatureTV.setText(String.valueOf(getCelcius(temp)) + "C");
            tempDay1TV.setText(String.valueOf(getCelcius(day1Max)) + "/" + String.valueOf(getCelcius(day1Min)));
            tempDay2TV.setText(String.valueOf(getCelcius(day2Max)) + "/" + String.valueOf(getCelcius(day2Min)));
            tempDay3TV.setText(String.valueOf(getCelcius(day3Max)) + "/" + String.valueOf(getCelcius(day3Min)));
            maxTempTV.setText(String.valueOf(max));
            minTempTV.setText(String.valueOf(min));
        }
        else{
            temperatureTV.setText(String.valueOf(temp) + "F");
            tempDay1TV.setText(String.valueOf(day1Max) + "/" + String.valueOf(day1Min));
            tempDay2TV.setText(String.valueOf(day2Max) + "/" + String.valueOf(day2Min));
            tempDay3TV.setText(String.valueOf(day3Max) + "/" + String.valueOf(day3Min));
            maxTempTV.setText(String.valueOf(max));
            minTempTV.setText(String.valueOf(min));
        }
    }

    public int getCelcius(int value){
        double farenhite = (value - 32) / 1.800;
        return (int) (Math.ceil(farenhite));
    }

}
