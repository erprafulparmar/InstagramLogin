package com.praful.instagram.login.model;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.pkmmte.view.CircularImageView;
import com.praful.instagram.login.R;
import com.praful.instagram.login.ui.RelationshipActivity;
import com.squareup.picasso.Picasso;


public class RelationShipAdapter extends RecyclerView.Adapter<RelationShipAdapter.ViewHolder> {

    Context mContext;
    ArrayList<HashMap<String, String>> mDataset;

    public RelationShipAdapter(Context applicationContext, ArrayList<HashMap<String, String>> usersInfo) {
        this.mContext = applicationContext;
        this.mDataset = usersInfo;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_relationship_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        HashMap<String, String> hashMapUserData = mDataset.get(position);
        holder.txtUsername.setText(hashMapUserData.get(RelationshipActivity.TAG_USERNAME));
        String strImageUrl = hashMapUserData.get(RelationshipActivity.TAG_PROFILE_PICTURE);
        Picasso.with(mContext).load(strImageUrl).error(R.drawable.user_icon).placeholder(R.drawable.user_icon).into(holder.imgUserProfile);
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CircularImageView imgUserProfile;
        public TextView txtUsername;

        public ViewHolder(View itemView) {
            super(itemView);
            imgUserProfile = itemView.findViewById(R.id.img_relation_user);
            txtUsername = itemView.findViewById(R.id.txt_relation_name);
        }
    }
}

