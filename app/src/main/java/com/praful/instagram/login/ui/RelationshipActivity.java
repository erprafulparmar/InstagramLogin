package com.praful.instagram.login.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.praful.instagram.login.R;
import com.praful.instagram.login.dbHandler.DatabaseHandler;
import com.praful.instagram.login.utils.ConstantData;
import com.praful.instagram.login.serviceController.NetworkCheck;
import com.praful.instagram.login.model.RelationShipAdapter;
import com.praful.instagram.login.serviceController.VolleyController;


public class RelationshipActivity extends AppCompatActivity {

    RecyclerView recyclerViewRelationShip;
    Toolbar toolbarRelationShip;
    Activity mContext;

    String strGetURL = "";
    String strGetTitle = "";

    ArrayList<HashMap<String, String>> arrayListRelationShip = new ArrayList<>();
    ProgressDialog pDialog;

    public static final String TAG_DATA = "data";
    public static final String TAG_ID = "id";
    public static final String TAG_PROFILE_PICTURE = "profile_picture";
    public static final String TAG_USERNAME = "username";
    public static final String TAG_FULL_NAME = "full_name";
    public static final String TAG_BIO = "bio";
    public static final String TAG_WEBSITE = "website";

    DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relationship);
        mContext = this;
        dbHandler = new DatabaseHandler(mContext);
        init();
        Intent iGet = getIntent();
        if (iGet != null) {
            strGetURL = iGet.getStringExtra("MAKE_URL");
            strGetTitle = iGet.getStringExtra("ACTION");
        }

        if (toolbarRelationShip != null) {
            toolbarRelationShip.setTitle(strGetTitle);
            setSupportActionBar(toolbarRelationShip);
            toolbarRelationShip.setNavigationIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_back));
            toolbarRelationShip.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        //TODO CALL SERVICE FETCH RELATION DATA...
        if (NetworkCheck.isNewtworkOK()) {
            CallRelationDataList();
        } else {
            Toast.makeText(mContext, R.string.err_msg_tryagain, Toast.LENGTH_SHORT).show();
        }
    }


    private void init() {
        toolbarRelationShip = findViewById(R.id.toolbar_relationship);
        recyclerViewRelationShip = findViewById(R.id.recycleview_relationship);
        recyclerViewRelationShip.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }

    private void CallRelationDataList() {
        try {
            String URL = strGetURL;
            pDialog = new ProgressDialog(mContext, R.style.DialogTheme);
            pDialog.setTitle("Processing...");
            pDialog.setMessage(getString(R.string.msg_pleasewait));
            pDialog.setCancelable(false);
            pDialog.show();

            StringRequest jsonObjRequest = new StringRequest(Request.Method.GET,
                    URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            parsingRelationShipData(response.trim());
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (pDialog.isShowing() && pDialog != null) {
                        pDialog.dismiss();
                    }
                    Toast.makeText(mContext, R.string.err_msg_tryagain, Toast.LENGTH_LONG).show();
                }
            }) {

                @Override
                public String getBodyContentType() {
                    return ConstantData.STR_ADDHEADER;
                }
            };
            jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                    90000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleyController.getInstance().addToRequestQueue(jsonObjRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parsingRelationShipData(String response) {
        try {
            if (!response.isEmpty()) {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_DATA);
                for (int data_i = 0; data_i < jsonArray.length(); data_i++) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    JSONObject subObject = jsonArray.getJSONObject(data_i);
                    hashMap.put(TAG_ID, subObject.getString(TAG_ID));
                    hashMap.put(TAG_PROFILE_PICTURE, subObject.getString(TAG_PROFILE_PICTURE));
                    hashMap.put(TAG_USERNAME, subObject.getString(TAG_USERNAME));
                    hashMap.put(TAG_FULL_NAME, subObject.getString(TAG_FULL_NAME));
                    hashMap.put(TAG_BIO, subObject.getString(TAG_BIO));
                    hashMap.put(TAG_WEBSITE, subObject.getString(TAG_WEBSITE));
                    arrayListRelationShip.add(hashMap);
                    //dbHandler.addRelationShipUser(arrayListRelationShip);
                }
                //TODO STATIC DATA FOR RELATION... FOLLOWING LIST PERMISSION PENDING REVIEW...
                dbHandler.deleteRelationshipUser();
                dbHandler.addRelationShipUser();
                ArrayList<HashMap<String, String>> relationShipUser = dbHandler.getRelationShipUser();
                if (relationShipUser.size() != 0) {
                    recyclerViewRelationShip.setAdapter(new RelationShipAdapter(mContext, relationShipUser));
                }
            } else {
                Toast.makeText(mContext, R.string.err_msg_wrong, Toast.LENGTH_SHORT).show();
            }
            if (pDialog.isShowing() && pDialog != null) {
                pDialog.dismiss();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
