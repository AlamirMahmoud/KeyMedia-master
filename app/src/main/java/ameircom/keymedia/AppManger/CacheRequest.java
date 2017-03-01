package ameircom.keymedia.AppManger;

import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.Map;

/**
 * Created by sotra on 10/2/2016.
 */
public class CacheRequest extends Request<NetworkResponse> {
    private final Response.Listener<NetworkResponse> mListener;
    private final Response.ErrorListener mErrorListener;
    boolean  chache = false;
    String url ;

    public CacheRequest(int method, String url, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.url = url ;
        this.mListener = listener;
        this.mErrorListener = errorListener;
    }


    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
        Map<String, String> headers = response.headers;
        Log.d("cachEntry",headers.get("Date")+"");

        if (cacheEntry == null) {
            Log.d("cachEntry null",cacheEntry+"");
            cacheEntry = new Cache.Entry();
        }else {
            chache = true ;
        }
        final long cacheHitButRefreshed = 5  * 1000; // in 5 sec cache will be hit, but also refreshed on background
        final long cacheExpired = 12 *24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
        long now = System.currentTimeMillis();
        final long softExpire = now + cacheHitButRefreshed;
        final long ttl = now + cacheExpired;
        cacheEntry.data = response.data;
        cacheEntry.softTtl = softExpire;
        cacheEntry.ttl = ttl;
        cacheEntry.etag = url ;
        String headerValue;
        headerValue = response.headers.get("Date");
        if (headerValue != null) {
            cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
        }
        headerValue = response.headers.get("Last-Modified");
        if (headerValue != null) {
            cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
        }
        cacheEntry.responseHeaders = response.headers;
        return Response.success(response, cacheEntry);
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        return super.parseNetworkError(volleyError);
    }

    @Override
    public void deliverError(VolleyError error) {
        if (!checkCache())
        mErrorListener.onErrorResponse(error);
    }
    public boolean checkCache (){
        try {
          chache =  this.getCacheEntry().etag  == url ? true : false ;
        }catch(NullPointerException e){
            chache  =  false ;
        }
            return  chache ;
    }

}