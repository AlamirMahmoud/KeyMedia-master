
package ameircom.keymedia.Activity;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import ameircom.keymedia.AppManger.AppController;
import ameircom.keymedia.AppManger.Config;
import ameircom.keymedia.Models.UserModel;
import ameircom.keymedia.R;
import ameircom.keymedia.adapter.CustomListAdapter;
import ameircom.keymedia.internal_db.Room_tabel;

public class Communicate extends AppCompatActivity {
    CustomListAdapter adapter ;
    UserModel userModel = AppController.getInstance().getPrefManager().getUser() ;
    ListView list;
    String[] itemname = {
            "Call us",
            "Message us",
            "KeyMedia Chat !"

    };

    Integer[] imgid = {
            R.drawable.imgtwo,
            R.drawable.imgone,
            R.drawable.mmnn,

    };
    ImageView i ;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communicate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         adapter = new CustomListAdapter(this, itemname, imgid );
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem = itemname[+position];
                Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
                if (Slecteditem.equals("Call us")) {
                    String number = "01113031657";
                    Uri call = Uri.parse("tel:" + number);
                    Intent surf = new Intent(Intent.ACTION_DIAL, call);
                    startActivity(surf);

                    Toast.makeText(Communicate.this, "Calling now !", Toast.LENGTH_SHORT).show();
                }
               else if (Slecteditem.equals("Message us")) {
                    Uri uri = Uri.parse("smsto:01113031657");
                    Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                    it.putExtra("sms_body", "The SMS text To Key Media");
                    startActivity(it);

                    Toast.makeText(Communicate.this, "Messaging now !", Toast.LENGTH_SHORT).show();
                    /*startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                            + "01113031657")));*/
                }
               else if (Slecteditem.equals(itemname[2])){
                        if (userModel.getType().equals("admin"))
                            startActivity(new Intent(getApplication(), MessegesTabActivity.class));
                            else
                     startActivityForResult(new Intent(getApplication(), MessageRoomDetailsActivity.class) , Config.MesaageDetailActivity);
                   clearNotfication();
                }
                else Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();


            }
        });

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("broadcast", "receive broadcast");
                Log.e("broadcast", intent.getAction() + "");
                // checking for type intent filter
                if (intent.getAction().equals(Config.UPDATE_USER_CHAT_ICON)) {
                    Log.d("broadcast", Config.UPDATE_USER_CHAT_ICON  );
//                    new Room_tabel().update_count(userModel.getId());
                    addNotfication();
                }

            }
        } ;


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.exit_prev, R.anim.enter_rev);
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


    protected void onResume() {
        super.onResume();
        Log.d("broadcast","register");

        // register new news feed  notification broadcast receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.UPDATE_USER_CHAT_ICON));

    }



//    @Override
//    protected void onPause() {
//        Log.d("broadcast","unregister");
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
//        super.onPause();
//    }

void addNotfication(){
       TextView chat_notifty = (TextView) list.getChildAt(2).findViewById(R.id.chat_notify) ;
        chat_notifty.setVisibility(View.VISIBLE);
    int current =   new Room_tabel().unseenCount(userModel.getId()) ;
    chat_notifty.setText(current+"");
    chat_notifty.setVisibility(View.VISIBLE);
}
    void clearNotfication(){
       TextView chat_notifty = (TextView) list.getChildAt(2).findViewById(R.id.chat_notify) ;
        chat_notifty.setVisibility(View.GONE);
        new Room_tabel().clear(userModel.getId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == Config.MesaageDetailActivity)
                clearNotfication();
    }
}

