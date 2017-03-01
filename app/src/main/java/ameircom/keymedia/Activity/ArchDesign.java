
package ameircom.keymedia.Activity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ameircom.keymedia.Activity.transition.FabTransform;
import ameircom.keymedia.AppManger.AndroidMultiPartEntity;
import ameircom.keymedia.AppManger.AppController;
import ameircom.keymedia.AppManger.Config;
import ameircom.keymedia.Models.FeedModel;
import ameircom.keymedia.R;
import ameircom.keymedia.adapter.NewsFeedAdapter;
import ameircom.keymedia.helper.RecyclerViewTouchHelper;

public class ArchDesign extends AppCompatActivity {
    SwipeRefreshLayout swipeRefresh ;
    ArrayList<FeedModel> data = new ArrayList<>() ; 
    RecyclerView news_recycler ; 
    NewsFeedAdapter adapter ;
    View add_product  ;
    String[] itemname  ;
    Integer[] imgid   ;
    ImageView i ;
    String type ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arch_design);
        add_product = findViewById(R.id.add_product) ;


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        news_recycler = (RecyclerView) findViewById(R.id.news_recycler_view);
        if (AppController.getInstance().getPrefManager().getUser().getType().equals("admin")){
            add_product.setVisibility(View.VISIBLE);
            add_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    add_product(view) ;
                }
            });
        }
        try {
          type = String.valueOf(getIntent().getExtras().getString("type"));
            getProducts();
        }catch (NullPointerException e){

        }
        setupRefreshLayout();
        recycleSetUP();

        
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            supportFinishAfterTransition();

        return  true ;
    }

    private void setupRefreshLayout(){
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefresh.setColorSchemeColors(ContextCompat.getColor(getApplicationContext(),R.color.red));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                get_intersted_courses_data();
            }
        });
    }

    private void endLoading() {
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(false);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            news_recycler.setAlpha(0);
            news_recycler.animate().alpha(1);
        }else {
            news_recycler.setVisibility(View.VISIBLE);
        }
    }

    void recycleSetUP() {
        adapter = new NewsFeedAdapter(this, data);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        news_recycler.setLayoutManager(layoutManager);
        news_recycler.setAdapter(adapter);
        Log.e("members " , data.size() +"");
        news_recycler.addOnItemTouchListener(new RecyclerViewTouchHelper(getApplicationContext(), news_recycler, new RecyclerViewTouchHelper.recyclerViewTouchListner() {
            @Override
            public void onclick(View child, int postion) {
                if (type.equals(Config.pa360)){
                        startPa360Details(child.findViewById(R.id.news_cover_img) , data.get(postion).getImg() );
                    Toast.makeText(ArchDesign.this, "here", Toast.LENGTH_LONG).show();
                }else {

                }


            }

            @Override
            public void onLongClick(View child, int postion) {

            }
        }));


    }
    private void get_intersted_courses_data() {
        endLoading();
    }

    void startPa360Details(View view , String img ){

        Intent intent = new Intent(getApplicationContext() , Pa360DetailsActivity.class);
        intent.putExtra("img" , img) ;
//        intent.putExtra("extra_id", extra.getCreator_id());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ArchDesign.this,
//                    view, getString(R.string.pa360_details_transition));
//
//
//            startActivity(intent,options.toBundle());
//        }else {
//            startActivity(intent);
//        }
        startActivity(intent);

    }
    void add_product(View view ){
        Intent intent = null;
            intent = new Intent(getApplicationContext(), Add_productActivity.class);
            intent.putExtra("type",type);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ArchDesign.this,
                    view, getApplication().getString(R.string.add_prod_fab_transition));
            FabTransform.addExtras(intent,
                    ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary),R.drawable.ic_add_white_36dp);

            startActivity(intent,options.toBundle());
        }else {
            startActivity(intent);
        }
    }
    public  void getProducts(){

        StringRequest feedRequest = new StringRequest(Request.Method.POST, Config.OPERATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                findViewById(R.id.progressBar1).setVisibility(View.GONE);
                try {
                    JSONObject resObj = new JSONObject(response);
                    JSONArray feedArr = resObj.getJSONArray("posts");
                    data.clear();
                    for (int i=0 ; i<feedArr.length();i++){
                        JSONObject currentElemnt =  feedArr.getJSONObject(i);
                        FeedModel currentFeed = new FeedModel();
                        currentFeed.setText(currentElemnt.getString("title"));
                        currentFeed.setImg(currentElemnt.getString("img"));
                        currentFeed.setType(currentElemnt.getString("type"));


                        data.add(currentFeed);
                        Log.e("adapter" , data.size() + " ");
                    }
                    adapter.notifyDataSetChanged();
                    adapter.refresh(data);
                    endLoading();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("error" , e.toString() + " ");

                    // getProducts();
                }

                Log.e("volley","respnse"+response.toString() );

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError){
                }
                if (error instanceof TimeoutError){
                    String message =  getString(R.string.NoConnection);
                    Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                }
                Log.e("volley", "error" + error.toString() + "  " + error.networkResponse);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> prams = new HashMap<>();
                prams.put("request", Config.GET_PROD);
                prams.put("type", type);
                return prams;
            }
        };
        int socketTimeout = 10000; // 10 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                10,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        feedRequest.setRetryPolicy(policy);
        Volley.newRequestQueue(getApplicationContext()).add(feedRequest);
    }


}
