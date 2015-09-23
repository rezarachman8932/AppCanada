package canada.about.com.app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import canada.about.com.app.AppController;
import canada.about.com.app.R;
import canada.about.com.app.adapters.ListAdapter;
import canada.about.com.app.models.Item;

public class ActMain extends AppCompatActivity {

    private static final int TIME_OUT = 5000;
    private static final String URL = "https://dl.dropboxusercontent.com/u/746330/facts.json";

    private boolean isInternetPresent = false;
    private ListView listContent;
    private ListAdapter adapter;
    private ArrayList<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        // Define all views
        defineViews();

        // Connection is number one
        if (isInternetPresent) {
            retrieveContent();
        }
    }

    private void defineViews() {
        isInternetPresent = AppController.getInstance()
                .getConnectionStatus().isConnectingToInternet();
        items = AppController.getInstance().getItemList();
        adapter = new ListAdapter(this, items);
        listContent = (ListView) findViewById(R.id.content_list);
    }

    private void parseData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArrayResponses = jsonObject.getJSONArray("rows");
            // Check content list
            if (jsonArrayResponses.length() > 0) {
                for (int i=0; i<jsonArrayResponses.length(); i++) {
                    JSONObject object = jsonArrayResponses.getJSONObject(i);
                    String title = object.getString("title");
                    String description = object.getString("description");
                    String imageHref = object.getString("imageHref");
                    items.add(new Item(title, description, imageHref));
                    Log.i(AppController.TAG, "Content List : " + items.get(i).getTitle());
                }
            }
            // Populate data to view
            if (items.size() > 0) {
                AnimationAdapter mAnimAdapter = new ScaleInAnimationAdapter(adapter);
                mAnimAdapter.setAbsListView(listContent);
                listContent.setAdapter(mAnimAdapter);
                mAnimAdapter.notifyDataSetChanged();
            }
            // Update title
            invalidateOptionsMenu();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void retrieveContent() {
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        parseData(s);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.getMessage();
            }
        });
        request.setShouldCache(true);
        request.setRetryPolicy(new DefaultRetryPolicy(
                TIME_OUT,   // Set time out
                0,          // We don't have to request automatically after time out
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().invalidate(URL, true);
        AppController.getInstance().getRequestQueue().getCache().get(URL);
        AppController.getInstance().addToRequestQueue(request, "queue");
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            if (AppController.getInstance().getRequestQueue().getCache().get(URL) != null) {
                String cachedResponse = new String(AppController.getInstance().
                        getRequestQueue().getCache().get(URL).data);
                JSONObject jsonObject = new JSONObject(cachedResponse);
                String title = jsonObject.getString("title");
                if (title.length() > 0) {
                    getSupportActionBar().setTitle(title);
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                if (isInternetPresent) {
                    retrieveContent();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
