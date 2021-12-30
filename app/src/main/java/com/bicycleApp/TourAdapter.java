package com.bicycleApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import Model.Tour;
import Model.Trip;

public class TourAdapter extends BaseAdapter {

    private Context context;
    private List<Tour> toursList;
    private LayoutInflater layoutInflater;

    public TourAdapter(Context context, List<Tour> toursList) {
        this.context = context;
        this.toursList = toursList;

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return toursList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        TourAdapter.ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new TourAdapter.ViewHolder();
            convertView = layoutInflater.inflate(R.layout.tour_adapter, viewGroup,false);
            viewHolder.index = (TextView) convertView.findViewById(R.id.text_number);
            viewHolder.date = (TextView) convertView.findViewById(R.id.text_element);
            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (TourAdapter.ViewHolder) convertView.getTag();

        viewHolder.index.setText(""+(position+1));
        viewHolder.date.setText(""+toursList.get(position).getDateWithoutSeconds());

        return convertView;
    }

    class ViewHolder{
        TextView index;
        TextView date;
    }
}