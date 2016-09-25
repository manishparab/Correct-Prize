package com.ksm.cp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ksm.cp.R;

import java.util.ArrayList;

/**
 * Created by mparab on 2/19/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<String> mDataset;
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.testlistviewitem, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    public MyAdapter(ArrayList<String> mDataset)
    {
        this.mDataset = mDataset;
    }

    public void remove(String item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        final String name = mDataset.get(position);
            holder.TextViewListviewItem.setText(mDataset.get(position));
        holder.TextViewListviewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(name);
            }
        });
    }

    @Override
    public int getItemCount() {
       return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
       public TextView TextViewListviewItem;
        public ViewHolder(View itemView) {
            super(itemView);
            this.TextViewListviewItem = (TextView)itemView.findViewById(R.id.TextViewListviewItem);
        }
    }
}
