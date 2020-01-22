package com.example.sih;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ShowImage extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    SliderLayout mDemoSlider;
    private static final String TAG = "ShowImage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
    context=this;
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        //replace the text with dates, i'm too sleepy to parse the date from original images
        Map<String,Integer> file_maps = returnHashMap();
        for(String name:file_maps.keySet())
        {
            Log.d(TAG, "onCreate: "+file_maps.get(name));
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView.description(name)
                    .image(file_maps.get(name)).setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener((BaseSliderView.OnSliderClickListener) this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener((ViewPagerEx.OnPageChangeListener) this);

    }
    @Override
    protected void onStop() {
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    private Map<String, Integer> returnHashMap() {
        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("2017/01/15-2",R.drawable.crop1);
        file_maps.put("2017/11/15-2",R.drawable.crop2);
        file_maps.put("2017/03/15-1",R.drawable.crop3);
        file_maps.put("2017/10/15-1",R.drawable.crop4);
        file_maps.put("2018/03/15-1",R.drawable.crop5);
        file_maps.put("2018/07/15-1",R.drawable.crop6);
        file_maps.put("2018/09/15-2",R.drawable.crop7);
        file_maps.put("2017/04/15-1",R.drawable.crop8);
        file_maps.put("2018/12/15-1",R.drawable.crop9);
        file_maps.put("2018/12/15-2",R.drawable.crop10);
        file_maps.put("2018/06/15-1",R.drawable.crop11);
        file_maps.put("2017/08/15-2",R.drawable.crop12);
        file_maps.put("2018/03/15-2",R.drawable.crop13);
        file_maps.put("2018/04/15-2",R.drawable.crop14);
        file_maps.put("2017/01/15-1",R.drawable.crop15);
        file_maps.put("2018/01/15-1",R.drawable.crop16);
        file_maps.put("2018/09/15-1",R.drawable.crop17);
        file_maps.put("2018/11/15-1",R.drawable.crop18);
        file_maps.put("2018/04/15-1",R.drawable.crop19);
        file_maps.put("2018/07/15-2",R.drawable.crop20);
        file_maps.put("2018/02/15-2",R.drawable.crop21);
        file_maps.put("2017/12/15-2",R.drawable.crop22);
        file_maps.put("2018/08/15-2",R.drawable.crop23);
        file_maps.put("2017/08/15-1",R.drawable.crop24);
        file_maps.put("2018/01/15-2",R.drawable.crop25);
        file_maps.put("2017/02/15-1",R.drawable.crop26);
        file_maps.put("2018/02/15-1",R.drawable.crop27);
        file_maps.put("2017/11/15-1",R.drawable.crop28);
        file_maps.put("2017/05/15-1",R.drawable.crop29);
        file_maps.put("2017/06/15-2",R.drawable.crop30);
        file_maps.put("2018/08/15-1",R.drawable.crop31);
        file_maps.put("2018/11/15-2",R.drawable.crop32);
        file_maps.put("2018/10/15-1",R.drawable.crop33);
        file_maps.put("2018/06/15-2",R.drawable.crop34);
        file_maps.put("2018/10/15-2",R.drawable.crop35);
        file_maps.put("2017/06/15-1",R.drawable.crop36);
        file_maps.put("2017/09/15-1",R.drawable.crop37);
        file_maps.put("2017/04/15-2",R.drawable.crop38);
        file_maps.put("2018/05/15-2",R.drawable.crop39);
        file_maps.put("2017/12/15-1",R.drawable.crop40);
        file_maps.put("2017/03/15-2",R.drawable.crop41);
        file_maps.put("2017/07/15-1",R.drawable.crop42);
        file_maps.put("2017/09/15-2",R.drawable.crop43);
        file_maps.put("2017/10/15-2",R.drawable.crop44);
        file_maps.put("2017/07/15-2",R.drawable.crop45);
        file_maps.put("2017/02/15-2",R.drawable.crop46);
        file_maps.put("2018/05/15-1",R.drawable.crop47);
        file_maps.put("2017/05/15-2",R.drawable.crop48);

        Map<String,Integer> sortedmap=new TreeMap<String,Integer>(file_maps);
        return sortedmap;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Constants.showToast(this,"This Image Was Taken On "+slider.getBundle().get("extra") ,Toast.LENGTH_LONG);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    Context context;
    CropInference cropDetails=new CropInference();
    void getDetails(){
        String url="";
        ServerRequest<JSONObject> tellIrisRequest = new ServerRequest<JSONObject>(Request.Method.POST,
                url,
                JSONObject.class,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        cropDetails=new Gson().fromJson(response.toString(),CropInference.class);
                        SingletonRequestQueue.getInstance(context)
                                .getRequestQueue()
                                .cancelAll(TAG);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Connection error. The response will be uploaded later", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                        //increment the retry count and make the request again
                    }
                })
                ;
        tellIrisRequest.setTag(TAG);
        SingletonRequestQueue.getInstance(context).addToRequestQueue(tellIrisRequest);

    }
    RecyclerView recyclerView;
    private void setUpRecyclerView(CropInference crop) {
//        recyclerView=findViewById(R.id.rv_my_bookings_list);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new CropInference(crop));
    }
}
