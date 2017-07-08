package com.dev3raby.infographic.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dev3raby.infographic.DataModels.SearchItem;
import com.dev3raby.infographic.R;

import java.util.ArrayList;

/**
 * Created by Ahmed Yehya on 07/07/2017.
 */

public class SearchListAdapter extends ArrayAdapter<SearchItem> {

    Context context;
    public SearchListAdapter(Context context, ArrayList<SearchItem> itemsArrayList) {
        super(context,0, itemsArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View lisItemView = convertView;

        if (lisItemView == null) {
            lisItemView = LayoutInflater.from(getContext()).inflate(R.layout.search_list_item, parent, false);
        }
        SearchItem currentItemSearch = getItem(position);
        SearchListAdapter.ViewHolder viewHolder = (SearchListAdapter.ViewHolder)lisItemView.getTag();

        if (viewHolder==null){
            viewHolder = new SearchListAdapter.ViewHolder();
            viewHolder.txName = (TextView) lisItemView.findViewById(R.id.tx_search_item);

            lisItemView.setTag(viewHolder);
        }

        viewHolder.txName.setText(currentItemSearch.getInfographicName());







        return lisItemView;
    }
    class ViewHolder {
        TextView txName;




    }
}
