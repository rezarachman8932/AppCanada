package canada.about.com.app;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import canada.about.com.app.Utils.ConnectionDetector;
import canada.about.com.app.models.Item;

/**
 * Created by user on 22/9/2015.
 */
public class AppController extends android.app.Application {

    private static AppController mInstance;
    private RequestQueue mRequestQueue;
    private ConnectionDetector mConnectionDetector;
    private ArrayList<Item> items;
    public static final String TAG = AppController.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public ArrayList<Item> getItemList() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public ConnectionDetector getConnectionStatus() {
        if (mConnectionDetector == null) {
            mConnectionDetector = new ConnectionDetector(this);
        }
        return mConnectionDetector;
    }

}
