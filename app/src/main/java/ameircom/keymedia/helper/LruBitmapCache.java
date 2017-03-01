package ameircom.keymedia.helper;

import android.graphics.Bitmap;
import android.util.LruCache;


/**
 * Created by paulodichone on 3/14/15.
 */
public class LruBitmapCache extends LruCache<String, Bitmap>  {



    public LruBitmapCache() {
        this(getDefaultLruCacheSize());
    }

    public LruBitmapCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }

    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 1;

        return cacheSize;
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

}
