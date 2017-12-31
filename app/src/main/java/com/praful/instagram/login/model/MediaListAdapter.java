package com.praful.instagram.login.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.praful.instagram.login.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class MediaListAdapter extends RecyclerView.Adapter<MediaListAdapter.ViewHolder> {

    Context mContext;
    ArrayList<String> mDataset;

    public MediaListAdapter(Context applicationContext, ArrayList<String> usersInfo) {
        this.mContext = applicationContext;
        this.mDataset = usersInfo;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_media_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String strUrl = mDataset.get(position);
        Picasso.with(mContext).load(strUrl).error(R.drawable.user_icon).placeholder(R.drawable.user_icon).into(holder.imgMedia);
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMedia;

        public ViewHolder(View itemView) {
            super(itemView);
            imgMedia = itemView.findViewById(R.id.ivImage);
        }
    }
}

