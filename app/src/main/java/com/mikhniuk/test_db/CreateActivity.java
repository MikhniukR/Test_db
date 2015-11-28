package com.mikhniuk.test_db;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class CreateActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    ArrayList<EditText> keys = new ArrayList<>();
    ArrayList<EditText> values = new ArrayList<>();
    ArrayList<ImageButton> buttons = new ArrayList<>();
    ArrayList<LinearLayout> layouts = new ArrayList<>();
    TextView message;
    ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create);
        message =  (TextView) findViewById(R.id.textView);
        linearLayout = (LinearLayout) findViewById(R.id.llv);
        imageView = (ImageView) findViewById(R.id.imageView);
        if(getIntent().hasExtra("id")){
            final RequestQueue queue = Volley.newRequestQueue(this);
            final String url = "http://46.101.205.23:4444/test_db/" +getIntent().getStringExtra("id");
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    Iterator<String> i=response.keys();
                    while(i.hasNext()){
                        String s=i.next();
                        try {
                            AddOne(message);
                            keys.get(keys.size()-1).setText(s);
                            values.get(values.size()-1).setText(response.getString(s));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    if(response.has("_attachments")){
                        try {
                            String a = response.get("_attachments").toString();
                            String im = "";
                            for(int j = 2; j < a.length();  j++){
                                if(a.charAt(j) == '"'){
                                    break;
                                }
                                im = im + a.charAt(j);
                            }
                            final ImageView mImageView;
                            String url2 = url + "/" + im;
                            mImageView = (ImageView) findViewById(R.id.imageView);
                            ImageRequest imageRequest = new ImageRequest(url2,
                                    new Response.Listener<Bitmap>() {
                                        @Override
                                        public void onResponse(Bitmap bitmap) {
                                            mImageView.setImageBitmap(bitmap);
                                        }
                                    }, 0, 0, null,
                                    new Response.ErrorListener() {
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    });
                            queue.add(imageRequest);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    message.setText(error.toString());
                }
            });

            queue.add(jsObjRequest);

        }
    }
    public void Cansel(View v){
        this.finish();
    }
    public void AddOne(View v){
        EditText editText1 = new EditText(this);
        editText1.setHint("key");
        editText1.setTextColor(Color.parseColor("#3f51b5"));
        editText1.setTextSize(10);
        keys.add(editText1);
        EditText editText2 = new EditText(this);
        editText2.setHint("value");
        editText2.setTextSize(10);
        editText2.setTextColor(Color.parseColor("#3f51b5"));
                values.add(editText2);
        final ImageButton button = new ImageButton(this);
        button.setId(R.id.button+(keys.size()));
        Bitmap bitmap;
        bitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.close);
        button.setImageBitmap(bitmap);
        button.setBackground(Drawable.createFromPath("#ffffff"));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < buttons.size(); i++) {
                    if(v.getId() == buttons.get(i).getId()){
                        layouts.get(i).removeAllViews();
                    }
                }
            }
        });
        buttons.add(button);
        LinearLayout linearLayoutH = new LinearLayout(this);
        switch (linearLayoutH.HORIZONTAL) {}
        linearLayoutH.addView(editText1);
        linearLayoutH.addView(editText2);
        linearLayoutH.addView(button);
        linearLayout.addView(linearLayoutH);
        layouts.add(linearLayoutH);

    }
    public void Submit(View v){
        String url = "http://46.101.205.23:4444/test_db";
        HashMap<String, String> params = new HashMap<String, String>();
        for(int i = 0; i < keys.size(); i++){
            if(keys.get(i).getText().equals("")&&keys.get(i).getText().equals("")){

            }else{
                params.put(keys.get(i).getText()+"",values.get(i).getText() + "");
            }
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response: %n %s", response.toString(4));
                            Toast toast = Toast.makeText(getApplicationContext(),response + "",Toast.LENGTH_LONG);
                            toast.show();
                            message.setText(response + "");
                        } catch (JSONException e) {
                            Toast toast = Toast.makeText(getApplicationContext(),e +"",Toast.LENGTH_LONG);
                            toast.show();                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Toast toast = Toast.makeText(getApplicationContext(),error +"",Toast.LENGTH_LONG);
                toast.show();
            }
        });
        queue.add(req);
    }

}
