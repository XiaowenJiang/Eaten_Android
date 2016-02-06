package com.example.w28l30.foursquareexample.Adapter;

/**
 * Created by W28L30 on 15/11/23.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.w28l30.foursquareexample.Model.NavDrawerItem;
import com.example.w28l30.foursquareexample.R;

import java.util.Collections;
import java.util.List;

import su.levenetc.android.badgeview.BadgeView;

/**
 * Created by Ravi Tamada on 12-03-2015.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    private MyViewHolder firstholder;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public List<NavDrawerItem> getData() {return data;}

    public MyViewHolder getFirstholder() {
        return firstholder;
    }

    public void clearCounter()
    {
        Log.d("Nav","clearCounter");
        firstholder.getCounter().setVisibility(View.INVISIBLE);
    }

    public void setCounter(int num)
    {
        firstholder.getCounter().setVisibility(View.VISIBLE);
        firstholder.getCounter().setValue(num);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
        Log.d("Nav","onBindViewHolder");
        holder.title.setText(current.getTitle());
        //set counter only at Home
        if(position == 0) {
            firstholder = holder;
            holder.counter.setTextSize(14);
        }
        holder.counter.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        BadgeView counter;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            counter = (BadgeView) itemView.findViewById(R.id.counter);
        }

        public BadgeView getCounter() {return counter;}
    }
}