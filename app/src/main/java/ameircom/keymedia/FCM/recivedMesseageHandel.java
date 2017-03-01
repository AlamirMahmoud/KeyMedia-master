package ameircom.keymedia.FCM;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Map;

import ameircom.keymedia.AppManger.Config;
import ameircom.keymedia.internal_db.Mess_tabel;
import ameircom.keymedia.internal_db.Room_tabel;

/**
 * Created by Sotraa on 6/15/2016.
 */
public class recivedMesseageHandel {

    public recivedMesseageHandel(Context context,Map<String,String> currentElemnt){
            Log.e("fcm " , currentElemnt.toString()) ;

        if (currentElemnt.get("action").equals("chat")){
            try {
                //add messege
                //notfy room
                Room_tabel room_tabel = new Room_tabel();

                    Mess_tabel  mess =  new Mess_tabel() ;


                    if (!mess.check(currentElemnt.get("mess_id"))){
                        mess.mess_id = currentElemnt.get("mess_id") ;
                        mess.user_id = currentElemnt.get("user_id") ;
                        mess.message = currentElemnt.get("message") ;
                        mess.type = Integer.parseInt(currentElemnt.get("type"));
                        mess.timeStamp = currentElemnt.get("timeStamp") ;
                        mess.user_name = currentElemnt.get("user_name") ;
                        mess.dir = 1 ;
                        mess.save() ;
                        Intent intent1 = new Intent ();
                        intent1.setAction(Config.UPDATE_USER_CHAT_ICON);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);
                        Intent intent2 = new Intent ();
                        intent2.setAction(Config.REFRESH_MESSAGES);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent2);

                    }
                    room_tabel.update_count(currentElemnt.get("user_id").toString());


            }catch (Exception e){}
//            new showNotificationHandel(context,currentElemnt.get("customer_name"),false);
        }


    }
}
