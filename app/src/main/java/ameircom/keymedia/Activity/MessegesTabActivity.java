package ameircom.keymedia.Activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ameircom.keymedia.AppManger.AppController;
import ameircom.keymedia.AppManger.Config;
import ameircom.keymedia.Models.UserModel;
import ameircom.keymedia.R;
import ameircom.keymedia.adapter.MessageRoomAdapter;
import ameircom.keymedia.helper.RecyclerViewTouchHelper;
import ameircom.keymedia.internal_db.Mess_tabel;
import ameircom.keymedia.internal_db.Room_tabel;


public class MessegesTabActivity extends Activity {
    View res_layout ;
    SwipeRefreshLayout swipeRefresh ;
    RecyclerView course_member_list;
    List<Room_tabel> course_member_data ;
    private MessageRoomAdapter adapter;
    Toolbar toolbar  ;

    public MessegesTabActivity() {
        // Required empty public constructor
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messeges_tab);
        
        toolbar = (Toolbar) findViewById(R.id.message_toolbar);
   

        setupRefreshLayout();
        course_member_data = new Room_tabel().data() ;
        get_users() ;
        course_member_list = (RecyclerView) findViewById(R.id.messegs_recycler_view);
        setupRefreshLayout();
        recycleSetUP();

    }

    private void setupRefreshLayout(){
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefresh.setColorSchemeColors(ContextCompat.getColor(getApplicationContext(),R.color.colorAccent));
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
        adapter = new MessageRoomAdapter(this, course_member_data);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        course_member_list.setLayoutManager(layoutManager);
        course_member_list.setAdapter(adapter);
        Log.e("members " , course_member_data.size() +"");


        course_member_list.addOnItemTouchListener(new RecyclerViewTouchHelper(getApplicationContext(), course_member_list, new RecyclerViewTouchHelper.recyclerViewTouchListner() {
            @Override
            public void onclick(View child, int postion) {
                Room_tabel current =  adapter.getItem(postion);
                startMessDetails(current.room_id);
            }

            @Override
            public void onLongClick(View child, int postion) {

            }
        }));


    }
    private void get_intersted_courses_data() {
        endLoading();
    }



    void startMessDetails( String  room){
        Intent intent = new Intent(getApplicationContext(),MessageRoomDetailsActivity.class);
        intent.putExtra("room_id",room) ;
//        intent.putExtra("extra_messeges", messageRoomModel);
//        ActivityOptions options = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
//                    sharedView, getString(R.string.chat_prof_transition_name));
//
//            startActivity(intent,options.toBundle());
//        }else {
//            startActivity(intent);
//        }
        startActivity(intent);

    }

    public  void get_users(){
        StringRequest feedRequest = new StringRequest(Request.Method.POST, Config.OPERATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resObj = new JSONObject(response);
                    JSONArray feedArr = resObj.getJSONArray("users");
                    for (int i=course_member_data.size() ; i<feedArr.length();i++){
                        JSONObject userObj = feedArr.getJSONObject(i);

                        UserModel user = new UserModel();
                        user.setId(  userObj.getString("userID"));
                        user.setName( userObj.getString("userName"));

                        Room_tabel room_tabel  =  new Room_tabel() ;
                        if (!room_tabel.check(user.getId())){
                            room_tabel.room_id = user.getId() ;
                            room_tabel.name = user.getName();
                            room_tabel.save();
                            FirebaseMessaging.getInstance().subscribeToTopic("room_"+user.getId());
                            course_member_data.add(room_tabel) ;
                        }
                    }
                    adapter.notifyDataSetChanged();



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
                prams.put("request", Config.GET_USERS);
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


}


