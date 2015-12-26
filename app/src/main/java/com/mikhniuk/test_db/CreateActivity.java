package com.mikhniuk.test_db;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class CreateActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    String url = "http://46.101.205.23:4444/test_db";
    ArrayList<EditText> keys = new ArrayList<>();
    ArrayList<EditText> values = new ArrayList<>();
    ArrayList<ImageButton> buttons = new ArrayList<>();
    ArrayList<LinearLayout> layouts = new ArrayList<>();
    ArrayList<ImageView> images = new ArrayList<>();
    ArrayList<String> imagesName = new ArrayList<>();
    TextView message;
    String id;
    String rev;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create);
        message =  (TextView) findViewById(R.id.textView);
        message.setText("laoding");
        linearLayout = (LinearLayout) findViewById(R.id.llv);
        if(getIntent().hasExtra("id")){
            final RequestQueue queue = Volley.newRequestQueue(this);
            final String url1 = url + "/" +  getIntent().getStringExtra("id");
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    Iterator<String> i=response.keys();
                    while(i.hasNext()){
                        String s=i.next();
                        try {
                            AddOne(message);
                            String a = response.getString(s);
                            if(s.equals("_id")){
                                id = a;
                            }else if(s.equals("_rev")){
                                rev = a;
                            }
                            keys.get(keys.size()-1).setText(s);
                            values.get(values.size()-1).setText(a);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    if(response.has("_attachments")){
                        try {
                            JSONObject atta = response.getJSONObject("_attachments");
                            i = atta.keys();
                            int n = 0;
                            while(i.hasNext()) {
                                n++;
                                final String sc = i.next();
                                final String url2 = url1 + "/" + sc;
                                ImageRequest imageRequest = new ImageRequest(url2,
                                        new Response.Listener<Bitmap>() {
                                            @Override
                                            public void onResponse(Bitmap bitmap) {
                                                addOneImage(bitmap, sc);
                                            }
                                        }, 0, 0, null,
                                        new Response.ErrorListener() {
                                            public void onErrorResponse(VolleyError error) {
                                                message.setText(error.toString() + " url:" + url2);
                                            }
                                        });
                                queue.add(imageRequest);
                            }
                            message.setText("Document have " + n + " images.");
                            /*String a = response.get("_attachments").toString();
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
                            int x = min(a.indexOf("png"),a.indexOf("gpg"))-1;
                            int k = 1;
                            while(x != -1){
                                String ans = "";
                                for(int c = x; c >=0; c--) {
                                    if (a.charAt(c) == '"') {
                                        break;
                                    }
                                    ans = a.charAt(c) + ans;
                                }
                                ans = ans + a.charAt(x+1)+a.charAt(x+2)+a.charAt(x+3) + "";
                                url2 = url + "/" + ans;
                                final ImageView m1ImageView;
                                if(k == 1){
                                    m1ImageView = (ImageView) findViewById(R.id.imageView2);
                                }else if(k == 2){
                                    m1ImageView = (ImageView) findViewById(R.id.imageView3);
                                }else if(k == 3){
                                    m1ImageView = (ImageView) findViewById(R.id.imageView4);
                                }else if(k == 4){
                                    m1ImageView = (ImageView) findViewById(R.id.imageView5);
                                }else{
                                    m1ImageView = (ImageView) findViewById(R.id.imageView6);
                                }
                                imageRequest = new ImageRequest(url2,
                                        new Response.Listener<Bitmap>() {
                                            @Override
                                            public void onResponse(Bitmap bitmap) {
                                                m1ImageView.setImageBitmap(bitmap);
                                            }
                                        }, 0, 0, null,
                                        new Response.ErrorListener() {
                                            public void onErrorResponse(VolleyError error) {

                                            }
                                        });
                                queue.add(imageRequest);
                                a = a.substring(0,x) + a.substring(x+4,a.length());
                                x = min(a.indexOf("png"), a.indexOf("gpg"))-1;
                                k++;*/

                           } catch (JSONException e1) {
                            e1.printStackTrace();
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
        message.setText("");
    }

    public void Cansel(View v){
        this.finish();
    }

    public void addOneImage(Bitmap bitmap, String name){
        TextView text = new TextView(this);
        text.setText(name);
        imagesName.add(name);
        text.setTextColor(Color.parseColor("#3f51b5"));
        linearLayout.addView(text);
        ImageView image = new ImageView(this);
        image.setImageBitmap(bitmap);
        images.add(image);
        linearLayout.addView(image);
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
                        keys.get(i).setText(null);
                        values.get(i).setText(null);
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

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void AddImage(View v){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
                try {
                    ImageView imageView = new ImageView(this);
                  /*  Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.close);*/
                    InputStream stream = getContentResolver().openInputStream(
                            data.getData());
                    imageView.setImageBitmap(BitmapFactory.decodeStream(stream));
                    linearLayout.addView(imageView);
                    images.add(imageView);
                    imagesName.add("userimage" + images.size() + ".jpg");
                    stream.close();
                    super.onActivityResult(requestCode, resultCode, data);
                }catch(SQLiteException e){
                    message.setText(e.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public void Submit(View v){
        if(images.size() == 0) {
            HashMap<String, String> params = new HashMap<String, String>();
            for (int i = 0; i < keys.size(); i++) {
                if (keys.get(i).getText().equals("") && keys.get(i) != null && keys.get(i).getText().toString().equals("null")
                        && keys.get(i).getText().toString().equals(null) ) {

                } else {
                    params.put(keys.get(i).getText() + "", values.get(i).getText() + "");
                }
            }
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(getApplicationContext(), response + "", Toast
                                    .LENGTH_LONG).show();
                            message.setText(response + "");
                            try {
                                String rev = response.get("rev").toString();
                                for (int i = 0; i < keys.size(); i++) {
                                    if (keys.get(i).getText().toString().equals("_rev")) {
                                        values.get(i).setText(rev);
                                        break;
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    message.setText(error.toString());
                }
            });
            queue.add(req);
        }else {
            final boolean[] mistake = {false};
            for (int i = 0; i < images.size(); i++) {
                message.setText("");
                String url1 = url + "/" + id + "/" + imagesName.get(i) + "?rev=" + rev;
                try {
                    ByteArrayInputStream bs;
                    if (imagesName.get(i).substring(imagesName.get(i).length() - 3).equals("png")) {
                        Bitmap bitmap = drawableToBitmap(images.get(i).getDrawable());
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                        byte[] bitmapdata = bos.toByteArray();
                        bs = new ByteArrayInputStream(bitmapdata);
                    } else {
                        Bitmap bitmap = drawableToBitmap(images.get(i).getDrawable());
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, bos);
                        byte[] bitmapdata = bos.toByteArray();
                        bs = new ByteArrayInputStream(bitmapdata);
                    }

                    final byte[] photoBytes = getBytes(bs);

                    RequestQueue queue1 = Volley.newRequestQueue(this);

                    StringRequest stringRequest = new StringRequest(Request.Method.PUT, url1,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                                    message.setText(message.getText() + "\n" + response.toString());
                                    String rev = "";
                                    int i = 0;
                                    while (response.charAt(i) != ',') {
                                        i++;
                                    }
                                    i++;
                                    while (response.charAt(i) != ',') {
                                        i++;
                                    }
                                    rev = response.substring(i + 8,response.length() - 3);
                                    for (int j = 0; j < keys.size(); j++) {
                                        if (keys.get(j).getText().toString().equals("_rev")) {
                                            values.get(j).setText(rev);
                                            break;
                                        }
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    mistake[0] = true;
                                    message.setText(error.toString());
                                }
                            }) {
                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            return photoBytes;
                        }
                    };
                    queue1.add(stringRequest);
                } catch (IOException e) {
                    message.setText(e.toString());
                }
            }
            if(!mistake[0]){
                Cansel(message);
            }
        }
    }

}
