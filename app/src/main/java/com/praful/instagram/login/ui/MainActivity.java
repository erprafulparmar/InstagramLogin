package com.praful.instagram.login.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pkmmte.view.CircularImageView;
import com.praful.instagram.login.R;
import com.praful.instagram.login.dbHandler.DatabaseHandler;
import com.praful.instagram.login.utils.ConstantData;
import com.praful.instagram.login.utils.InstagramApp;
import com.praful.instagram.login.model.MediaListAdapter;
import com.praful.instagram.login.serviceController.VolleyController;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbarDashboard;
    FloatingActionButton floatingActionButtonPost;
    CircularImageView imgUserProfile;
    TextView txtPostCount, txtFollowersCount, txtFollowingCount, txtFullName, txtBio;

    Activity mContext;
    String strMakeRelationURL = "";

    InstagramApp mApp;

    Button btnLogin;
    ProgressBar progressBarMedia;
    RecyclerView recyclerViewMedia;
    ArrayList<String> imageThumbList = new ArrayList<>();

    public static final String TAG_DATA = "data";
    public static final String TAG_IMAGES = "images";
    public static final String TAG_THUMBNAIL = "thumbnail";
    public static final String TAG_URL = "url";

    TextView btnFollowers, btnFollwing;
    RelativeLayout layoutConnection, layoutConnectionSuccess;
    HashMap<String, String> userInfoHashmap = new HashMap<>();

    DatabaseHandler dbHandler;

    Handler handler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            if (msg.what == InstagramApp.WHAT_FINALIZE)
            {
                userInfoHashmap = mApp.getUserInfo();
                dbHandler.addLoggedUser(userInfoHashmap);
                settingUserData();
                imageThumbList.clear();
                CallMediaDataListService();
            } else if (msg.what == InstagramApp.WHAT_FINALIZE) {
                Toast.makeText(MainActivity.this, R.string.err_msg_tryagain, Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        dbHandler = new DatabaseHandler(mContext);
        init();
        if (toolbarDashboard != null) {
            toolbarDashboard.setTitle("Instagram");
            setSupportActionBar(toolbarDashboard);
        }

        mApp = new InstagramApp(this, ConstantData.CLIENT_ID, ConstantData.CLIENT_SECRET, ConstantData.CALLBACK_URL);
        mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {
            @Override
            public void onSuccess()
            {
                layoutConnection.setVisibility(View.GONE);
                layoutConnectionSuccess.setVisibility(View.VISIBLE);
                mApp.fetchUserName(handler);
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        if (mApp.hasAccessToken())
        {
            layoutConnection.setVisibility(View.GONE);
            layoutConnectionSuccess.setVisibility(View.VISIBLE);
            mApp.fetchUserName(handler);
        }
    }


    private void settingUserData() {
        HashMap<String, String> loggedUser = userInfoHashmap;
        String strImageUrl = loggedUser.get(InstagramApp.TAG_PROFILE_PICTURE);
        Picasso.with(this).load(strImageUrl).error(R.drawable.user_icon).placeholder(R.drawable.user_icon).into(imgUserProfile);
        toolbarDashboard.setTitle(loggedUser.get(InstagramApp.TAG_USERNAME));
        txtFullName.setText(loggedUser.get(InstagramApp.TAG_FULL_NAME));
        txtBio.setText(loggedUser.get(InstagramApp.TAG_BIO));
        txtPostCount.setText(loggedUser.get(InstagramApp.TAG_MEDIA));
        txtFollowersCount.setText(loggedUser.get(InstagramApp.TAG_FOLLOWS));
        txtFollowingCount.setText(loggedUser.get(InstagramApp.TAG_FOLLOWED_BY));
    }


    private void init() {
        floatingActionButtonPost = findViewById(R.id.fab_post);
        toolbarDashboard = findViewById(R.id.toolbar_dashboard);
        txtFullName = findViewById(R.id.txt_fullname_dashboard);
        txtBio = findViewById(R.id.txt_bio_dashboard);
        imgUserProfile = findViewById(R.id.img_userprofile_dashboard);
        txtPostCount = findViewById(R.id.txt_post_dashboard);
        txtFollowersCount = findViewById(R.id.txt_followers_dashboard);
        txtFollowingCount = findViewById(R.id.txt_following_dashboard);
        layoutConnection = findViewById(R.id.layout_connection);
        layoutConnectionSuccess = findViewById(R.id.layout_connection_success);
        progressBarMedia = findViewById(R.id.progress_media);
        recyclerViewMedia = findViewById(R.id.recycleview_medialist);
        recyclerViewMedia.setLayoutManager(new GridLayoutManager(mContext, 3));
        btnLogin = findViewById(R.id.btn_login);
        btnFollowers = findViewById(R.id.btn_followers);
        btnFollwing = findViewById(R.id.btn_following);
        floatingActionButtonPost.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnFollwing.setOnClickListener(this);
        btnFollowers.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                mApp.authorize();
                break;
            case R.id.btn_followers:
                setOnFollowerClick();
                break;
            case R.id.btn_following:
                setOnFollowingClick();
                break;
            case R.id.fab_post:
                Snackbar snackbar = Snackbar.make(layoutConnectionSuccess, "Add New Post Feature", Snackbar.LENGTH_LONG);
                snackbar.show();
                break;
        }
    }


    private void setOnFollowerClick() {
        if (userInfoHashmap.size() != 0) {
            strMakeRelationURL = "https://api.instagram.com/v1/users/"
                    + userInfoHashmap.get(InstagramApp.TAG_ID)
                    + "/follows?access_token=" + mApp.getTOken();
            Intent iFollowers = new Intent(mContext, RelationshipActivity.class);
            iFollowers.putExtra("MAKE_URL", strMakeRelationURL);
            iFollowers.putExtra("ACTION", "Followers");
            startActivity(iFollowers);
        } else {
            Toast.makeText(mContext, R.string.err_msg_wrong, Toast.LENGTH_SHORT).show();
        }
    }


    private void setOnFollowingClick() {
        if (userInfoHashmap.size() != 0) {
            strMakeRelationURL = "https://api.instagram.com/v1/users/"
                    + userInfoHashmap.get(InstagramApp.TAG_ID)
                    + "/followed-by?access_token=" + mApp.getTOken();
            Intent iFollowing = new Intent(mContext, RelationshipActivity.class);
            iFollowing.putExtra("MAKE_URL", strMakeRelationURL);
            iFollowing.putExtra("ACTION", "Following");
            startActivity(iFollowing);
        } else {
            Toast.makeText(mContext, R.string.err_msg_wrong, Toast.LENGTH_SHORT).show();
        }
    }


    private void DisconnectUser()
    {
        if (mApp.hasAccessToken())
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    MainActivity.this);
            builder.setMessage("Logged Out ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    mApp.resetAccessToken();
                                    layoutConnection.setVisibility(View.VISIBLE);
                                    layoutConnectionSuccess.setVisibility(View.GONE);
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            final AlertDialog alert = builder.create();
            alert.show();
        }
    }


    private void CallMediaDataListService() {
        try {
            String MEDIA_URL = "https://api.instagram.com/v1/users/self/media/recent/?access_token=" +mApp.getTOken();
            progressBarMedia.setVisibility(View.VISIBLE);
            recyclerViewMedia.setVisibility(View.GONE);

            StringRequest jsonObjRequest = new StringRequest(Request.Method.GET,
                    MEDIA_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            parsingRelationShipData(response.trim());
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBarMedia.setVisibility(View.VISIBLE);
                    recyclerViewMedia.setVisibility(View.GONE);
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

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject data_obj = jsonArray.getJSONObject(i);
                    JSONObject images_obj = data_obj.getJSONObject(TAG_IMAGES);
                    JSONObject thumbnail_obj = images_obj.getJSONObject(TAG_THUMBNAIL);
                    String str_url = thumbnail_obj.getString(TAG_URL);
                    imageThumbList.add(str_url);
                }
                recyclerViewMedia.setAdapter(new MediaListAdapter(mContext, imageThumbList));
            } else {
                Toast.makeText(mContext, R.string.err_msg_wrong, Toast.LENGTH_SHORT).show();
            }
            progressBarMedia.setVisibility(View.GONE);
            recyclerViewMedia.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menudashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            DisconnectUser();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
