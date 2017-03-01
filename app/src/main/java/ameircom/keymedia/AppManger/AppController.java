package ameircom.keymedia.AppManger;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import ameircom.keymedia.helper.LruBitmapCache;
import ameircom.keymedia.internal_db.Mess_tabel;
import ameircom.keymedia.internal_db.Room_tabel;


public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    LruBitmapCache mLruBitmapCache;
    private MyPreferenceManager pref;
    private RequestQueue mRequestQueue;
    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
//        ActiveAndroid.initialize(this);
        Log.e("appcontroler " , "instatnce create ") ;
        Configuration.Builder config = new Configuration.Builder(this);
        config.addModelClasses(Room_tabel.class, Mess_tabel.class);
        ActiveAndroid.initialize(config.create());

    }

    public MyPreferenceManager getPrefManager() {
        if (pref == null) {
            pref = new MyPreferenceManager(this);
        }

        return pref;
    }


    public LruBitmapCache getLruBitmapCache() {
        if (mLruBitmapCache == null)
        {
            mLruBitmapCache = new LruBitmapCache();
            Log.e("cache","new Cache");
        }else{
            Log.e("cache","ex Cache");
        }
        return this.mLruBitmapCache;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


}