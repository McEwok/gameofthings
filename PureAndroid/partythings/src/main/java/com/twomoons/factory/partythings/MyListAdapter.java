package com.twomoons.factory.partythings;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Joshua on 11/5/2015.
 */
public class MyListAdapter extends BaseAdapter {
    private Activity _Context;
    private List<String> _List;
    private LayoutInflater _LayoutInflator = null;
    public MyListAdapter(Activity context, List<String> list){
        _Context = context;
        _List = list;
        _LayoutInflator = (LayoutInflater) _Context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return _List.size();
    }

    @Override
    public Object getItem(int position) {
        return _List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {
        View v = convertview;
        CompleteListViewHolder viewHolder;
        if (convertview == null){
            LayoutInflater li = (LayoutInflater) _Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.list_layout, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }
        viewHolder.mTVItem.setText(_List.get(position));
        return v;
    }
}

class CompleteListViewHolder {
    public TextView mTVItem;
    public CompleteListViewHolder(View base){
        mTVItem = (TextView) base.findViewById(R.id.listTV);
    }
}