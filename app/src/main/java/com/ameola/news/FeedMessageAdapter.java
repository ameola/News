package com.ameola.news;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FeedMessageAdapter extends RecyclerView.Adapter<FeedMessageAdapter.FeedMessageViewHolder> {

    private List<FeedMessage> dataList;

    public FeedMessageAdapter(List<FeedMessage> dataList) {
        this.dataList = dataList;
    }

    @Override
    public FeedMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.news_row, parent, false);
        return new FeedMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedMessageViewHolder holder, int position) {
        holder.txtTitle.setText(dataList.get(position).getTitle());
        holder.txtDescription.setText(dataList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class FeedMessageViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtDescription;

        FeedMessageViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            txtDescription = (TextView) itemView.findViewById(R.id.txt_description);
        }
    }
}