package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bicycleApp.R;

import java.util.List;

import Model.Trip;

public class TourAdapter extends BaseAdapter {

    private Context context;
    private List<Trip> toursList;
    private LayoutInflater layoutInflater;

    public TourAdapter(Context context, List<Trip> toursList) {
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
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.tour_adapter, viewGroup,false);
            viewHolder.index = (TextView) convertView.findViewById(R.id.tour_adapter_text_number);
            viewHolder.date = (TextView) convertView.findViewById(R.id.tour_adapter_text_date);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tour_adapter_text_title);
            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.index.setText(""+(position+1));
        viewHolder.date.setText(""+toursList.get(position).getDateWithoutTime());
        viewHolder.title.setText(toursList.get(position).getTitle());

        return convertView;
    }

    class ViewHolder{
        TextView index;
        TextView date;
        TextView title;
    }
}