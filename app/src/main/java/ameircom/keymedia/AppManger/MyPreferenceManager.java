package ameircom.keymedia.AppManger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.IntentCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

import ameircom.keymedia.Activity.LoginActivity;
import ameircom.keymedia.Models.UserModel;


public class MyPreferenceManager {

    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "keymedia";

    // All Shared Preferences Keys
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_TYPE = "user_type";
    private static final String KEY_USER_PHONE = "user_phone";


    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void storeUser(UserModel user) {
        editor.clear();
        editor.commit();
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_TYPE , user.getType());
        editor.putString(KEY_USER_PHONE , user.getPhone());
        editor.putString(KEY_USER_EMAIL , user.getEmail());
        editor.commit();


        Log.e(TAG, "User is stored in shared preferences. " + user.getName() + " ," + user.getType() );
    }

    public UserModel getUser() {
        if (pref.getString(KEY_USER_ID, null) != null) {
            String id, name,type,phone,email;
            id = pref.getString(KEY_USER_ID, null);
            name = pref.getString(KEY_USER_NAME, null);
            type = pref.getString(KEY_USER_TYPE, null);
            phone = pref.getString(KEY_USER_PHONE, null);
            email = pref.getString(KEY_USER_EMAIL, null);

            UserModel user = new UserModel();
            user.setId(id);
            user.setName(name);
            user.setType(type);
            user.setPhone(phone);
            user.setName(email);
            return user;
        }
        return null;
    }
    public void clear() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic("room_"+getUser().getId());
        editor.clear();
        editor.commit();
        Intent intent = new Intent(_context, LoginActivity.class);
        ComponentName cn = intent.getComponent();
        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
        _context.startActivity(mainIntent);
    }
}
