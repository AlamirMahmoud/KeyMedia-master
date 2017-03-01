package ameircom.keymedia.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ameircom.keymedia.AppManger.Config;
import ameircom.keymedia.Models.FeedModel;
import ameircom.keymedia.adapter.CustomListAdapter;
import ameircom.keymedia.R;


public class DepartmentsActivity extends AppCompatActivity{
    ArrayList<FeedModel> media  = new ArrayList<>();
    ArrayList<FeedModel> adv  = new ArrayList<>();
    ArrayList<FeedModel> mob  = new ArrayList<>();
    ArrayList<FeedModel> arc  = new ArrayList<>();
    ArrayList<FeedModel> p360  = new ArrayList<>();

    ListView list;
    String[] itemname ={
            "Media",
            "Advertising",
            "Mobile Application",
            "Architectural design",
            "Panorama 360"
    };

    Integer[] imgid={
            R.drawable.one,
            R.drawable.two,
            R.drawable.threetwoooo,
            R.drawable.four,
            R.drawable.bb,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CustomListAdapter adapter=new CustomListAdapter(this, itemname, imgid);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem= itemname[+position];
                if ( Slecteditem.equals("Media"))
                {
                    Intent i = new Intent(DepartmentsActivity.this , ArchDesign.class);
                    i.putExtra("type" , Config.media);
                    startActivity(i);

                }
                else if ( Slecteditem.equals("Advertising"))
                {
                    Intent i = new Intent(DepartmentsActivity.this , ArchDesign.class);
                    i.putExtra("type" , Config.adv);
                    startActivity(i);
                }
             else    if ( Slecteditem.equals("Mobile Application"))
                {
                    Intent i = new Intent(DepartmentsActivity.this , ArchDesign.class);
                    i.putExtra("type" , Config.mob);
                    startActivity(i);
                }
              else   if ( Slecteditem.equals("Architectural design"))
                {
                    Intent i = new Intent(DepartmentsActivity.this , ArchDesign.class);
                    i.putExtra("type" , Config.arc);
                    startActivity(i);
                }
              else   if ( Slecteditem.equals(itemname[4]))
                {
                    Intent i = new Intent(DepartmentsActivity.this , ArchDesign.class);
                    i.putExtra("type" , Config.pa360);
                    startActivity(i);
                }
                else                 Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true ;

        }
        return false ;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.exit_prev, R.anim.enter_rev);
    }


}