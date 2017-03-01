package ameircom.keymedia.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ameircom.keymedia.AppManger.AppController;
import ameircom.keymedia.AppManger.Config;
import ameircom.keymedia.Models.MessageModel;
import ameircom.keymedia.Models.UserModel;
import ameircom.keymedia.R;
import ameircom.keymedia.adapter.MessagesAdapter;
import ameircom.keymedia.internal_db.Mess_tabel;
import ameircom.keymedia.internal_db.Room_tabel;

public class MessageRoomDetailsActivity extends AppCompatActivity {
    SwipeRefreshLayout swipeRefresh ;
    RecyclerView course_member_list;
    List<Mess_tabel> messageModels ;
    private MessagesAdapter adapter;
    EditText message_input ;
    View send_btt ;
    private static String today;
    BroadcastReceiver broadcastReceiver ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message_room_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        course_member_list = (RecyclerView) findViewById(R.id.message_recycler_view);
        message_input = (EditText) findViewById(R.id.message_input);
        send_btt = findViewById(R.id.send_btt);

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

        setupRefreshLayout();
        recycleSetUP();

        send_btt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Mess_tabel  mess_tabel =  new Mess_tabel() ;
                mess_tabel.message= (message_input.getText().toString());
                if (!AppController.getInstance().getPrefManager().getUser().getType().equals("admin"))
                mess_tabel.user_id = (AppController.getInstance().getPrefManager().getUser().getId());
                else {
                    mess_tabel.user_id = getIntent().getExtras().getString("room_id") ;
                }
                mess_tabel.timeStamp = currentDateFormat() ;
                mess_tabel.type = Config.TEXT_MESSAGE_TYPE ;
                mess_tabel.dir = 2;
                adapter.insertData(mess_tabel);
                message_input.setText("");
                send_message(mess_tabel) ;
            }
        });

         broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("broadcast", "receive broadcast");
                Log.e("broadcast", intent.getAction() + "");
                // checking for type intent filter
                if (intent.getAction().equals(Config.REFRESH_MESSAGES)) {
                    Log.d("broadcast", Config.REFRESH_MESSAGES);
//                    new Room_tabel().update_count(userModel.getId());
                   getData();
                }


            }
        };

    }

    public  void send_message(final Mess_tabel mess_tabel){
        StringRequest feedRequest = new StringRequest(Request.Method.POST, Config.OPERATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resObj = new JSONObject(response);
                    JSONObject message = resObj.getJSONObject("mess");

//                        JSONObject mess_obj = feedArr.getJSONObject(i);

                        Mess_tabel message_db_obj = new Mess_tabel();
                        message_db_obj.message= (  message.getString("message"));
                        message_db_obj.user_name= ( message.getString("user_name"));
                        message_db_obj.mess_id= ( message.getString("mess_id"));
                        message_db_obj.user_id= ( message.getString("user_id"));
                        message_db_obj.type= ( message.getInt("type"));
                        message_db_obj.timeStamp= ( message.getString("timeStamp"));
                    message_db_obj.dir = 2;
                        message_db_obj.save() ;




                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("volley","respnse"+response.toString() );

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {
                    String message =  getString(R.string.NoConnection);
                    Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                }



                Log.e("volley", "error" + error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> prams = new HashMap<>();
                prams.put("request", String.valueOf(Config.SEND_MESS));
                prams.put("message", mess_tabel.message);
                prams.put("user_id", mess_tabel.user_id);
                prams.put("type", String.valueOf(mess_tabel.type));
                Log.e("messge param", prams.toString());

                return prams;
            }
        };
        int socketTimeout = 10000; // 10 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                10,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        feedRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(feedRequest);
    }


    public static String getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "";

        today = today.length() < 2 ? "0" + today : today;

        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
            String date1 = format.format(date);
            timestamp = date1.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
    }
    public static String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
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
            course_member_list.setAlpha(0);
            course_member_list.animate().alpha(1);
        }else {
            course_member_list.setVisibility(View.VISIBLE);
        }
    }

    void recycleSetUP() {

            messageModels = new ArrayList<>() ;
        adapter = new MessagesAdapter(this, messageModels);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext() ,LinearLayoutManager.VERTICAL , true);
        course_member_list.setLayoutManager(layoutManager);
        course_member_list.setAdapter(adapter);
        Log.e("members " , messageModels.size() +"");
        getData();


    }
    private void get_intersted_courses_data() {
        endLoading();
    }
    void getData(){
        int prev = messageModels.size();
        if (getIntent().getExtras() != null)
            messageModels = new Mess_tabel().list_of_room_data(getIntent().getExtras().getString("room_id")) ;

        else
            messageModels =  new Mess_tabel().list_of_room_data(AppController.getInstance().getPrefManager().getUser().getId()) ;
        for (int i=prev ; i<messageModels.size() ; i++){
            adapter.insertData(messageModels.get(i));
        }


    }

    protected void onResume() {
        super.onResume();
        Log.d("broadcast","register");

        // register new news feed  notification broadcast receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.REFRESH_MESSAGES));

    }



    @Override
    protected void onPause() {
        Log.d("broadcast","unregister");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }


}
