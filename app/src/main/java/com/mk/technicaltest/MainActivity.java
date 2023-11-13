package com.mk.technicaltest;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    ArrayList<SliderData> sliderDataArrayList;
    SliderView sliderView;
    String product_base_url, banner_base_url;
    RequestQueue queue;
    JSONObject config;
    JSONArray products, banners;
    String msg;
    RecyclerView recyclerView;
    List<ProductData> productDataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sliderDataArrayList = new ArrayList<>();

        sliderView = findViewById(R.id.slider);

        productDataList = new ArrayList<>();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);


        queue = Volley.newRequestQueue(this);

        Map<String, String> params = new HashMap<String, String>();
        params.put("country", "1");

        String url = "https://www.icelandic.ae/App_v1/App_Launch";
        Log.i("TAG", "httpCall: " + url);

        CustomRequest request = new CustomRequest(Request.Method.POST, url, params, response -> {
            if (response != null) try {
                //handle your response
                Log.i("TAG", "httpCall: response" + response);

                msg = response.getString("message");
                if (!msg.isEmpty() && msg.equalsIgnoreCase("success")) {
                    config = response.getJSONObject("config");
                    product_base_url = config.getString("prduct_base_url");
                    banner_base_url = config.getString("banner_base_url");

                    Log.i("TAG", "httpCall:  product_base_url " + product_base_url + "\nbanner_base_url" + banner_base_url);
                    banners = response.getJSONArray("banners");
                    products = response.getJSONArray("products");

                    for (int i = 0, size = banners.length(); i < size; i++) {
                        JSONObject banner = banners.getJSONObject(i);
                        sliderDataArrayList.add(new SliderData(banner_base_url + banner.getString("url")));
                        Log.i("TAG", "httpCall:  sliderDataArrayList [" + i + "]" + banner_base_url + banner.getString("url"));
                    }

                    for (int i = 0, size = products.length(); i < size; i++) {
                        JSONObject prod = products.getJSONObject(i);
                         Log.i("TAG", "httpCall: productsSliderDataArrayList [" + i + "]" + product_base_url + prod.getString("image"));
                        productDataList.add(new ProductData(product_base_url + prod.getString("image"),
                                prod.getString("name"),
                                prod.getString("price"),
                                prod.getString("description")));
                     }

                    updateActivity();
                }
            } catch (Exception ex) {
                Log.e("TAG", "httpCall: exception: ", ex);
                ex.printStackTrace();
            }
            else Log.e("TAG", "httpCall: Response is NULL");
        }, null);
        queue.add(request);
    }

    public void updateActivity() {
        SliderAdapter adapter = new SliderAdapter(this, sliderDataArrayList);

        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        sliderView.setSliderAdapter(adapter);

        sliderView.setScrollTimeInSec(3);

        sliderView.setAutoCycle(true);

        sliderView.startAutoCycle();

        ProductAdapter productAdapter = new ProductAdapter(productDataList, getApplication(),null);
        recyclerView.setAdapter(productAdapter);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));

    }

}
